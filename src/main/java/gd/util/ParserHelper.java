package gd.util;

import gd.model.Company;
import gd.model.Content;
import org.json.JSONArray;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by anle on 4/10/16.
 */
public class ParserHelper {
    public static SortedMap<Content, Integer> groupByDate(JSONArray contents, Company company){
        SortedMap<Content, Integer> groupByDate = new TreeMap<Content, Integer>();
        for (int i = 0; i < contents.length(); i++) {
            String reviewDate = contents.getJSONObject(i).getString("reviewDateTime").substring(0,7);
            Content newContent = new Content(company.getId(), company.getName(), company.getIndustry(), company.getSize(), reviewDate);

            if(groupByDate.containsKey(newContent)){
                groupByDate.put(newContent, groupByDate.get(newContent) + 1);
            } else {
                groupByDate.put(newContent, 1);
            }
        }

        return groupByDate;
    }
}
