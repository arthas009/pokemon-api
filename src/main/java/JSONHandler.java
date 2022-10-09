import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

public class JSONHandler {

    /**
     * getJsonFromURL
     * Retrieves json object taken from a given URL
     *
     * @param url url to retrieve json data
     * @return JSON Object of taken json data
     */
    public static JSONObject getJsonFromURL(URL url) throws IOException {
        String pageText;
        URLConnection conn = url.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            pageText = reader.lines().collect(Collectors.joining("\n"));
        }
        return new JSONObject(pageText);
    }

    /**
     * extractResults
     * Extracts json array based on key 'result' from json objects
     *
     * @param object json object coming from URL's response or from the inside of another object
     * @return Instance of Json array found by key 'result'
     */
    public static JSONArray extractResults(JSONObject object) {
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equals("results")) {
                return (JSONArray) object.get(key);
            }
        }
        return null;
    }

    /**
     * extractTypes
     * Extracts json array based on key 'types' from json objects
     *
     * @param object json object coming from the inside of another object
     * @return Instance of Json array found by key 'types'
     */
    public static JSONArray extractTypes(JSONObject object) {
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equals("types")) {
                return (JSONArray) object.get(key);
            }
        }
        return null;
    }

    /**
     * extractPokeUrlMapFromResults
     * Places all pokemon name/url page combinations to Java HashMap
     *
     * @param results json array extracted from a part of json object
     * @return Instance of HashMap<String,String> including pokemon name, url combination.
     */
    public static HashMap<String, String> extractPokeUrlMapFromResults(JSONArray results) {
        HashMap<String, String> pokeUrlMap = new HashMap<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject innerObject = results.getJSONObject(i);
            Iterator<?> innerKeys = innerObject.keys();
            String currentPoke = "";
            while (innerKeys.hasNext()) {
                String key = (String) innerKeys.next();
                if (key.equals("name")) {
                    currentPoke = innerObject.get(key).toString();
                }
                if (key.equals("url")) {
                    pokeUrlMap.put(currentPoke, innerObject.get(key).toString());
                }
            }
        }
        return pokeUrlMap;
    }

}
