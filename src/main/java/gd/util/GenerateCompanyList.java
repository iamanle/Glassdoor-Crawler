package gd.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by anle on 4/21/16.
 */
public class GenerateCompanyList {
    public static void main(String[] args){
        String jsonString = "";

        JSONArray jsonArr = new JSONArray(jsonString);
        System.out.println();

        Set<String> companies = new HashSet<>();
        for(int i = 0; i < jsonArr.length(); i++){
            JSONObject a = (JSONObject)jsonArr.get(i);
            if(!companies.contains(a.getString("name"))){
                companies.add(a.getString("name"));
                String competitors = a.getString("competitors");
                for(String c : competitors.split(",")){
                    companies.add(c);
                }
            }
        }

        for(String s : companies){
            System.out.println("\""+s+"\""+",");
        }
    }
}
