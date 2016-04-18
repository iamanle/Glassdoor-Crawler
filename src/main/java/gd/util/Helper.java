package gd.util;

import gd.model.Company;
import gd.model.Content;
import gd.model.ContentList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by anle on 4/10/16.
 */
public class Helper {
    private static final String USER_AGENT = "Mozilla/5.0";
    public static String sendGet(String url){
        String result ="";
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            result = response.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static ContentList contentToContentList(SortedMap<Content, Integer> reviews, SortedMap<Content, Integer> interviews, Company company){
        SortedMap<String, Integer> reviewMap = new TreeMap<>();
        for(Map.Entry<Content,Integer> e : reviews.entrySet()){
            reviewMap.put(e.getKey().getDate(), e.getValue());
        }

        SortedMap<String, Integer> interviewMap = new TreeMap<>();
        for(Map.Entry<Content,Integer> e : interviews.entrySet()){
            interviewMap.put(e.getKey().getDate(), e.getValue());
        }

        ContentList contentList = new ContentList(company.getId(),company.getName()
                ,company.getIndustry(),company.getSize(),company.getCompetitors(), reviewMap, interviewMap);
        return contentList;
    }
}
