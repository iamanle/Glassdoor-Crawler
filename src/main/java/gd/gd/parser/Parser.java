package gd.gd.parser;

import gd.model.Company;
import gd.model.Content;
import org.json.JSONException;

import java.util.SortedMap;

/**
 * Created by anle on 4/10/16.
 */
public interface Parser {
    SortedMap<Content, Integer> parse(String url, Company company) throws JSONException;
    String constructURL(String employerId, String page, String apiSignature);
    int getTotalPage(String url);
}
