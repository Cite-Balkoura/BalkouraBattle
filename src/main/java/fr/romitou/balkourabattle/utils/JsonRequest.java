package fr.romitou.balkourabattle.utils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import fr.romitou.balkourabattle.BalkouraBattle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonRequest {

    private static final String CHALLONGE_URL = BalkouraBattle.getInstance().getConfigFile().getString("challonge.default_endpoint");
    private static final String CHALLONGE_KEY = BalkouraBattle.getInstance().getConfigFile().getString("challonge.key");
    private static final HttpRequestFactory REQUEST_FACTORY = new NetHttpTransport().createRequestFactory();

    private static JSONArray getJsonArray(String jsonString) throws IOException {
        return new JacksonFactory()
                .createJsonParser(jsonString)
                .parseAndClose(JSONArray.class);
    }

    private static JSONObject getJsonObject(String jsonString) throws IOException {
        return new JacksonFactory()
                .createJsonParser(jsonString)
                .parseAndClose(JSONObject.class);
    }

    public static <T> T getJsonRequest(String endpoint, boolean isArray) {
        try {
            HttpRequest request = REQUEST_FACTORY
                    .buildGetRequest(new GenericUrl(CHALLONGE_URL + endpoint + ".json")
                            .set("api_key", CHALLONGE_KEY));
            String rawResponse = request.execute().parseAsString();
            return (T) (isArray ? getJsonArray(rawResponse) : getJsonObject(rawResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJsonRequest(String endpoint) {
        return getJsonRequest(endpoint, false);
    }

    public static JSONObject postJsonRequest(String endpoint, @Nullable Map<String, Object> data) {
        try {
            data = (data == null ? new HashMap<>() : data);
            data.put("api_key", CHALLONGE_KEY);
            HttpContent content = new JsonHttpContent(new JacksonFactory(), data);
            HttpRequest request = REQUEST_FACTORY
                    .buildPostRequest(new GenericUrl(CHALLONGE_URL + endpoint + ".json"), content);
            return getJsonObject(request.execute().parseAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject postJsonRequest(String endpoint) {
        return postJsonRequest(endpoint, null);
    }

}
