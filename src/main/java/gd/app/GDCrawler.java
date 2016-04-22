package gd.app;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.util.JSON;
import gd.gd.parser.Interview;
import gd.gd.parser.Parser;
import gd.gd.parser.Review;
import gd.model.*;
import gd.util.Helper;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.*;

/**
 * 1) Get JSON string
 * 2) Extract date
 * 3) Update DB count if exist, if not init with 1
 */

public class GDCrawler {
    public static void main(String[] args) throws Exception {
        boolean testMode = false;
        String apiSignature = "";
        String dbCredential = "";

        List<String> employerNames = new ArrayList<>(Arrays.asList("Tullett Prebon"));

        for(String employerName: employerNames){
            crawlAllAndSaveWithCompanyName(employerName, apiSignature, dbCredential, testMode);
        }
    }

    public static void crawlAllAndSaveWithCompanyName(String employerName,String apiSignature, String dbCredential, boolean testMode){
        if(checkDup(employerName, dbCredential)) return;
        String employerId = getCompanyId(employerName);
        crawlAllAndSave(employerId,apiSignature,dbCredential,testMode);
    }

    private static String getCompanyId(String employerName) {
        String url ="";
        try{
            url = "http://api.glassdoor.com/api/api.htm?t.p=61363&t.k=hjsbj6thhmA&userip=0.0.0.0&useragent=&format=json&v=1&action=employers&q="
                    + URLEncoder.encode(employerName,"UTF-8");
        }catch(Exception e){

        }

        System.out.println(url);
        String jsonString = Helper.sendGet(url);
        JSONObject json = new JSONObject(jsonString);
        JSONObject responseObject = json.getJSONObject("response");

        JSONArray employers = responseObject.getJSONArray("employers");
        String id = ((JSONObject)employers.get(0)).get("id").toString();
        return id;
    }

    public static void crawlAllAndSave(String employerId, String apiSignature, String dbCredential, boolean testMode){
        Company company = getCompanyInfo(employerId, apiSignature);

        SortedMap<Content, Integer> reviews = crawl(company, ContentType.REVIEW, apiSignature, testMode);
        SortedMap<Content, Integer> interviews = crawl(company, ContentType.INTERVIEW, apiSignature, testMode);

        ContentList contentList = Helper.contentToContentList(reviews, interviews, company);
        List<Document> trainData = createTrainData(contentList);

        saveToMongoDB(contentList, trainData, dbCredential);
    }

    /**
     * employerID can be found on Glassdoor website
     * For example Salesforce is 11159
     * When testing please set total page to sth small
     */
    private static SortedMap<Content, Integer> crawl(Company company, ContentType contentType, String apiSignature, boolean testMode){
        String employerId = company.getId();
        Parser parser;
        int totalPage = 5;

        if(contentType == ContentType.REVIEW){
            parser = new Review();
        } else {
            parser = new Interview();
        }

        String url = parser.constructURL(employerId, "1", apiSignature);
        if(!testMode)
            totalPage = parser.getTotalPage(url);

        System.out.println("Crawling " + totalPage + " pages of " + contentType);
        SortedMap<Content, Integer> contents = new TreeMap<Content, Integer>();

        // Crawl each page and add data to contents
        for (int i = 1; i <= totalPage; i++) {
            String pageURL = parser.constructURL(employerId,i+"", apiSignature);
            System.out.println(pageURL);
            SortedMap<Content, Integer> content = parser.parse(pageURL, company);
            for(Map.Entry<Content,Integer> c : content.entrySet()){
                if(contents.containsKey(c.getKey())){
                    contents.put(c.getKey(), c.getValue() + contents.get(c.getKey()));
                }else{
                    contents.put(c.getKey(),c.getValue());
                }
            }

            try{
                Thread.sleep(1000);
            } catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Total entry: " + contents.size());

        return contents;
    }

    private static Company getCompanyInfo(String employerId, String apiSignature){
        String url = "https://www.glassdoor.com/api-internal/api.htm?version=1&responseType=json&t.p=16&t.k=fz6JLNgLgVs" +
                "&action=employer-overview&s.expires=1399139157952" +
                "&signature=" +apiSignature +
                "&locale=en_IN" +
                "&employerId="+employerId;

        String jsonString = Helper.sendGet(url);
        JSONObject json = new JSONObject(jsonString);
        JSONObject responseObject = json.getJSONObject("response");

        String name = responseObject.getString("name");
        String industry = responseObject.getString("industry");
        String size = responseObject.getString("size");
        String competitors = responseObject.getString("competitors");

        Company company = new Company(employerId, name, industry, size, competitors);
        return company;
    }


    /**
     * TODO: Check if entry is in DB to update before inserting
     */
    private static void saveToMongoDB(ContentList contentList, List<Document> trainData, String credential) {
        MongoClientURI uri = new MongoClientURI("mongodb://" + credential + "@52.32.81.35/?authSource=admin");
        MongoClient mc = new MongoClient(uri);
        MongoDatabase db = mc.getDatabase("glassdoor");

        // Save as Train Data collection
        db.getCollection(contentList.getCompanyName()).insertMany(trainData);

        // Save to UI content collection
        String jsonString = new Gson().toJson(contentList);
        DBObject dbo = (DBObject) JSON.parse(jsonString);
        Document document = new Document(dbo.toMap());
        db.getCollection("uiContent").insertOne(document);
    }

    private static boolean checkDup(String companyName, String credential){
        MongoClientURI uri = new MongoClientURI("mongodb://" + credential + "@52.32.81.35/?authSource=admin");
        MongoClient mc = new MongoClient(uri);
        MongoDatabase db = mc.getDatabase("glassdoor");

        MongoIterable<String> collectionIterable = db.listCollectionNames();
        for(String s : collectionIterable){
            if (s.equals(companyName)){
                System.out.println("There is duplication for " + companyName);
                return true;
            }
        }

        return false;
    }

    private static List<Document> createTrainData(ContentList contentList){
        SortedMap<String,Integer> mergedContent = new TreeMap<>(contentList.getInterviews());
        contentList.getReviews().forEach((k,v) -> mergedContent.merge(k,v,Integer::sum));

        double sizeWeight = getSizeWeight(contentList.getSize());
        List<Document> documents = new ArrayList<>();
        int lastCount = 0;

        for(Map.Entry<String,Integer> e : mergedContent.entrySet()){
            int newCount = e.getValue();

            Document document = new Document();
            document.put("time", e.getKey());
            document.put("oldCount", lastCount);
            document.put("newCount", newCount);
            document.put("sizeWeight", sizeWeight);

            double interactionPoint = lastCount == 0 ? 0 : (newCount/lastCount) * Math.log10(newCount) * sizeWeight;
            document.put("interactionPoint", interactionPoint);

            documents.add(document);
            lastCount = e.getValue();
        }

        return documents;
    }

    private static double getSizeWeight(String size){
        double weight = 1;

        switch (size.toLowerCase()){
            case "10000+ employees":
                weight = 1.4;
                break;
            case "5001 to 10000 employees":
                weight = 5.3;
                break;
            case "1001 to 5000 employees":
                weight = 11.8;
                break;
            case "501 to 1000 employees":
                weight = 100.6;
                break;
            case "201 to 500 employees":
                weight = 120.4;
                break;
            case "51 to 200 employees":
                weight = 182.6;
                break;
            case "1 to 50 employees":
                weight = 272;
                break;
            default:
                weight = 1;
        }

        return weight;
    }
}
