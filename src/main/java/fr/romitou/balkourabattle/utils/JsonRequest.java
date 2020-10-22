package fr.romitou.balkourabattle.utils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import fr.romitou.balkourabattle.BalkouraBattle;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonRequest {

    private static final String CHALLONGE_URL = BalkouraBattle.getInstance().getConfigFile().getString("challonge.url");
    private static final String CHALLONGE_KEY = BalkouraBattle.getInstance().getConfigFile().getString("challonge.key");

    public static JSONObject getJsonRequest(String endpoint) {
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
        try {
            HttpRequest request = requestFactory
                    .buildGetRequest(new GenericUrl(CHALLONGE_URL + endpoint + ".json")
                            .set("api_key", CHALLONGE_KEY));
            String rawResponse = request.execute().parseAsString();
            assert rawResponse != null;
            return (JSONObject) JSONValue.parseWithException(rawResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
