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
        URL url = new URL(mainURL);
        // Get base object from given URL in task
        JSONObject json_object = JSONHandler.getJsonFromURL(url);
        // Get an JSONArray including names and URL information of pokemons
        JSONArray results_json_array = JSONHandler.extractResults(json_object);
        // Place names and URLs to HashMap to make everything clear
        HashMap<String, String> pokeUrlMap = JSONHandler.extractPokeUrlMapFromResults(results_json_array);

        List<String> normalPokemons = findNormalPokemons(pokeUrlMap);
        System.out.println(normalPokemons);
    }

    /**
     * findNormalPokemons
     * Finds pokemons with normal attribute or one of their attribute is normal
     * @param pokeUrlMap HashMap of pokemons including their name and base url
     * @return List of pokemon names including normal attribute
     */
    public static List<String> findNormalPokemons(HashMap<String, String> pokeUrlMap) {
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
