package fr.romitou.balkourabattle.utils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import fr.romitou.balkourabattle.BalkouraBattle;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;

public class JsonRequest {

    private static final String CHALLONGE_URL = BalkouraBattle.getInstance().getConfigFile().getString("challonge.url");
    private static final String CHALLONGE_KEY = BalkouraBattle.getInstance().getConfigFile().getString("challonge.key");
    private static final HttpRequestFactory REQUEST_FACTORY = new NetHttpTransport().createRequestFactory();

    public static JSONObject getJsonRequest(String endpoint) {
        try {
            HttpRequest request = REQUEST_FACTORY
                    .buildGetRequest(new GenericUrl(CHALLONGE_URL + endpoint + ".json")
                            .set("api_key", CHALLONGE_KEY));
            String rawResponse = request.execute().parseAsString();
            assert rawResponse != null;
            return (JSONObject) JSONValue.parseWithException(rawResponse);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject postJsonRequest(String endpoint, Map<String, Object> data) {
        try {
            data.put("api_key", CHALLONGE_KEY);
            HttpContent content = new JsonHttpContent(new JacksonFactory(), data);
            HttpRequest request = REQUEST_FACTORY
                    .buildPostRequest(new GenericUrl(CHALLONGE_URL + endpoint + ".json"), content);
            String rawResponse = request.execute().parseAsString();
            assert rawResponse != null;
            return (JSONObject) JSONValue.parseWithException(rawResponse);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
