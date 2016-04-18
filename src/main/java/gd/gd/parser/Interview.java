package gd.gd.parser;

import gd.model.Company;
import gd.model.Content;
import gd.util.Helper;
import gd.util.ParserHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.SortedMap;

/**
 * Created by anle on 4/10/16.
 */
public class Interview implements Parser{
    public SortedMap<Content, Integer> parse(String url, Company company) throws JSONException {
        String jsonString = Helper.sendGet(url);
        JSONObject json = new JSONObject(jsonString);
        JSONObject responseObject = json.getJSONObject("response");
        JSONArray interviews = responseObject.getJSONArray("interviews");
        return ParserHelper.groupByDate(interviews, company);
    }

    public String constructURL(String employerId, String page, String apiSignature){
        return "https://www.glassdoor.com/api-internal/api.htm?version=1" +
                "&responseType=json" +
                "&t.p=16&t.k=fz6JLNgLgVs&s.expires=1397183249715" +
                "&signature=" + apiSignature +
                "&locale=en_US" +
                "&action=employer-interview&includeReviewText=true"+
                "&sort.sortType=RD" +
                "&sort.ascending=false" +
                "&pageNumber=" + page +
                "&employerId=" + employerId;
    }

    public int getTotalPage(String url){
        String jsonString = Helper.sendGet(url);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject responseObject = jsonObject.getJSONObject("response");
        JSONObject employerObject = responseObject.getJSONObject("employer");

        int interviewCount = employerObject.getInt("interviewCount");
        System.out.println("Review count: " + interviewCount);
        return (int)Math.ceil((double)interviewCount/(double)10);
    }
}
