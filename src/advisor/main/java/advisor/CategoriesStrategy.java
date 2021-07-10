package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


public class CategoriesStrategy extends ReactionStrategies {
    private static final String end_URL = "/v1/browse/categories";

    @Override
    UserReply produce(UserRequest userRequest) {
        HttpResponse<String> response = sendCategoriesRequest(userRequest);
        saveCategories(userRequest, response);

        return new UserReply();
    }

    public static HttpResponse<String> sendCategoriesRequest(UserRequest userRequest) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + userRequest.getUser().getAccessToken())
                .uri(URI.create(Main.comLineArgs.get("-resource") + end_URL))
                .GET()
                .build();
        return SpotifyRequest.safeSendRequest(httpRequest);
    }

    public static void saveCategories(UserRequest userRequest, HttpResponse<String> response) {
        Map<String, String> responseCategories = Map.of("", "");
        if (response.statusCode() == 200) {
            responseCategories = parseCategoriesFromResponse(response);
            Main.currentPages.printCurrent();
        } else {
            SpotifyRequest.errorPrint(response);
        }
        userRequest.setCurrentCategories(responseCategories);
    }

    public static Map<String, String> parseCategoriesFromResponse(HttpResponse<String> response) {
        PlaylistPages<CategoriesEntry> currentPlaylistPages = new PlaylistPages<>();
        String responseBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonElement categories = jsonObject.get("categories");
        JsonObject jsonCategoriesObject = categories.getAsJsonObject();
        JsonArray jsonArray = jsonCategoriesObject.get("items").getAsJsonArray();
        Iterator<JsonElement> categoriesIterator = jsonArray.iterator();
        Map<String, String> savedCategories = new Hashtable<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            CategoriesEntry categoryEntry = new CategoriesEntry();
            JsonObject element = categoriesIterator.next().getAsJsonObject();
            categoryEntry.setCategory(element.get("name").getAsString());
            savedCategories.put(element.get("name").getAsString(), element.get("id").getAsString());

            currentPlaylistPages.addMusicEntry(categoryEntry);
        }
        Main.currentPages = currentPlaylistPages;
        return savedCategories;
    }
}