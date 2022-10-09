import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Main {
    static String mainURL = "https://pokeapi.co/api/v2/pokemon?limit=30";

    public static void main(String[] args) throws IOException {
        System.out.println("Pokemons with normal attribute are: ");
        System.out.println(findNormalPokemons());
    }

    /**
     * findNormalPokemons
     * Finds pokemons with normal attribute or one of their attribute is normal
     *
     * @return List of pokemon names including normal attribute
     */
    public static List<String> findNormalPokemons() {
        URL url;
        JSONObject json_object = null;
        // Get base object from given URL in task
        try {
            url = new URL(mainURL);
            json_object = JSONHandler.getJsonFromURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get an JSONArray including names and URL information of pokemons
        assert json_object != null;
        JSONArray results_json_array = JSONHandler.extractResults(json_object);
        // Place names and URLs to HashMap to make everything clear
        assert results_json_array != null;
        HashMap<String, String> pokeUrlMap = JSONHandler.extractPokeUrlMapFromResults(results_json_array);

        List<String> returnList = new ArrayList<>();

        // Iterate through all name/url combinations to check pokemon is normal or not
        pokeUrlMap.forEach((key, value) -> {
            URL pokeURL = null;
            JSONObject pokeObject = null;
            JSONArray pokeTypes = null;
            try {
                // Get json object from pokemon's own page
                pokeURL = new URL(value);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                // Get current Pokemon's url page json
                assert pokeURL != null;
                pokeObject = JSONHandler.getJsonFromURL(pokeURL);
                // Get types object from Json document
                pokeTypes = JSONHandler.extractTypes(pokeObject);

                // Iterate through all keys in current json object to retrieve 'slot' and 'type' data. Keep in mind that
                // there can be more than one object in each pokeType object
                for (int i = 0; i < pokeTypes.length(); i++) {
                    JSONObject innerObject = pokeTypes.getJSONObject(i);
                    Iterator<?> innerKeys = innerObject.keys();
                    while (innerKeys.hasNext()) {
                        String pokeTypeKey = (String) innerKeys.next();
                        if (pokeTypeKey.equals("type") && innerObject.get(pokeTypeKey).toString().contains("normal")) {
                            returnList.add(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return returnList;
    }
}
