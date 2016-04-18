package gd.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by anle on 4/12/16.
 */
public class JSONToCSV {
    public static void main(String[] args){
        String jsonString = "";
        JSONObject json = new JSONObject(jsonString);
        String regexp =  "(\\d+-\\d+).*?(\\d+)";
        Pattern pattern = Pattern.compile(regexp);
        SortedMap<String, List<Integer>> content = new TreeMap<>();

        // Review
        JSONObject reviews = json.getJSONObject("reviews");
        String[] entries = reviews.toString().split(",");

        for(String e :entries){
            Matcher matcher = pattern.matcher(e);
            if(matcher.find()){
                content.put(matcher.group(1), Arrays.asList(Integer.parseInt(matcher.group(2)),0));
            }
        }

        // Interview
        JSONObject interviews = json.getJSONObject("interviews");
        entries = interviews.toString().split(",");

        for(String e :entries){
            Matcher matcher = pattern.matcher(e);
            if(matcher.find()){
                String date = matcher.group(1);
                Integer count = Integer.parseInt(matcher.group(2));

                if(content.containsKey(date)){
                    List<Integer> list = new ArrayList<>(content.get(date));
                    list.remove(1);
                    list.add(count);
                    content.put(date,list);
                }else{
                    content.put(date, Arrays.asList(0,count));
                }
            }
        }

        /**
         * "size" : "201 to 500 Employees": 6
         size" : "501 to 1000 Employees”:4.5
         "size" : "1001 to 5000 Employees": 3
         "size" : "5001 to 10000 Employees": 1.5
         "size" : "10000+ Employees": 1
         */
        System.out.println("Month,Content,Size,Point");
        for(Map.Entry<String,List<Integer>> e : content.entrySet()){
            System.out.println(e.getKey()+","+(e.getValue().get(0)+e.getValue().get(1))+",6");

        }
    }
}
