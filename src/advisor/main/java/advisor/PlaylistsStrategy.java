package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;


public class PlaylistsStrategy extends ReactionStrategies {
    private final String middle_URL = "/v1/browse/categories/";
    private final String end_URL = "/playlists";

    @Override
    UserReply produce(UserRequest userRequest) {
        if(userRequest.getCurrentCategories().isEmpty()){
            HttpResponse<String> response = CategoriesStrategy.sendCategoriesRequest(userRequest);
            CategoriesStrategy.saveCategories(userRequest, response);
        }
        String category = readCategoryFromInput(userRequest);
        if(category.equals("")){
            System.out.println("Unknown category name.");
            return new UserReply();
        }
        HttpResponse<String> response = sendPlaylistsRequest(userRequest, category);
        if (response.statusCode() == 200 && !response.body().startsWith("{\"error\"")) {
            parsePlaylistsFromResponse(response);
            Main.currentPages.printCurrent();
        } else {
            SpotifyRequest.errorPrint(response);
        }
        return new UserReply();
    }


    HttpResponse<String> sendPlaylistsRequest(UserRequest userRequest, String categoryID) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + userRequest.getUser().getAccessToken())
                .uri(URI.create(Main.comLineArgs.get("-resource") + middle_URL + categoryID + end_URL))
                .GET()
                .build();
        return SpotifyRequest.safeSendRequest(httpRequest);
    }

    private String readCategoryFromInput(UserRequest userRequest){
        String category = userRequest.getArgs()[0];
        return userRequest.getCurrentCategories().getOrDefault(category, "");
    }

    public void parsePlaylistsFromResponse(HttpResponse<String> response){
        PlaylistPages<PlaylistEntry> currentPlaylistEntry = new PlaylistPages<>();
        String responseBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonElement categories = jsonObject.get("playlists");
        JsonObject jsonCategoriesObject = categories.getAsJsonObject();
        JsonArray jsonArray = jsonCategoriesObject.get("items").getAsJsonArray();
        Iterator<JsonElement> categoriesIterator = jsonArray.iterator();
        for (int i = 0; i < jsonArray.size(); i++) {
            PlaylistEntry playlistEntry = new PlaylistEntry();
            JsonObject element = categoriesIterator.next().getAsJsonObject();
            playlistEntry.setName(element.get("name").getAsString());
            JsonObject externalURLs = element.get("external_urls").getAsJsonObject();
            playlistEntry.setReference(externalURLs.get("spotify").getAsString() + "\n");
            currentPlaylistEntry.addMusicEntry(playlistEntry);
        }
        Main.currentPages = currentPlaylistEntry;
    }
}