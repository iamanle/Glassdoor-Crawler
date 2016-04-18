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
public class Review implements Parser{
    public SortedMap<Content, Integer> parse(String url, Company company) throws JSONException {
        String jsonString = Helper.sendGet(url);
        JSONObject json = new JSONObject(jsonString);
        JSONObject responseObject = json.getJSONObject("response");
        JSONArray reviews = responseObject.getJSONArray("reviews");
        return ParserHelper.groupByDate(reviews, company);
    }

    public String constructURL(String employerId, String page, String apiSignature){
        return "https://www.glassdoor.com/api-internal/json/employer/reviewList.htm?t.s=j" +
                "&locale=en_US" +
                "&t.p=16&t.a=i" +
                "&t.k=fz6JLNgLgVs" +
                "&signature=" + apiSignature +
                "&sort.sortType=RD" +
                "&sort.ascending=false" +
                "&pageNumber=" + page +
                "&employerId=" + employerId;
    }

    public int getTotalPage(String url){
        String jsonString = Helper.sendGet(url);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject responseObject = jsonObject.getJSONObject("response");
        int reviewsCount = responseObject.getInt("totalNumberOfPages");
        System.out.println("Review count: " + reviewsCount);
        return reviewsCount;
    }
}

