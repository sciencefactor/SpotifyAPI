package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;

public class FeaturedStrategy extends ReactionStrategies {
    private final String end_URL = "/v1/browse/featured-playlists";

    @Override
    UserReply produce(UserRequest userRequest) {
        HttpResponse<String> response = sendFeaturedRequest(userRequest);
        if (response.statusCode() == 200) {
            parseFeaturedFromResponse(response);
            Main.currentPages.printCurrent();
        } else {
            SpotifyRequest.errorPrint(response);
        }
        return new UserReply();
    }


    HttpResponse<String> sendFeaturedRequest(UserRequest userRequest) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + userRequest.getUser().getAccessToken())
                .uri(URI.create(Main.comLineArgs.get("-resource") + end_URL))
                .GET()
                .build();
        return SpotifyRequest.safeSendRequest(httpRequest);
    }

    public void parseFeaturedFromResponse(HttpResponse<String> response){
        PlaylistPages<FeaturedEntry> currentPlaylistPages = new PlaylistPages<>();
        String responseBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonElement categories = jsonObject.get("playlists");
        JsonObject jsonCategoriesObject = categories.getAsJsonObject();
        JsonArray jsonArray = jsonCategoriesObject.get("items").getAsJsonArray();
        Iterator<JsonElement> categoriesIterator = jsonArray.iterator();

        for (int i = 0; i < jsonArray.size(); i++) {
            FeaturedEntry featuredEntry = new FeaturedEntry();
            JsonObject element = categoriesIterator.next().getAsJsonObject();
            featuredEntry.setName(element.get("name").getAsString());
            JsonObject externalURLs = element.get("external_urls").getAsJsonObject();
            featuredEntry.setReference(externalURLs.get("spotify").getAsString() + "\n");

            currentPlaylistPages.addMusicEntry(featuredEntry);
        }

        Main.currentPages = currentPlaylistPages;
    }

}