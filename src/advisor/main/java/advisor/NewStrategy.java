package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;


public class NewStrategy extends ReactionStrategies {
    private final String end_URL = "/v1/browse/new-releases";

    @Override
    UserReply produce(UserRequest userRequest) {
        HttpResponse<String> response = sendNewsRequest(userRequest);
        if (response.statusCode() == 200) {
            parseNewsFromResponse(response);
            Main.currentPages.printCurrent();
        } else {
            SpotifyRequest.errorPrint(response);
        }
        return new UserReply();
    }


    HttpResponse<String> sendNewsRequest(UserRequest userRequest) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + userRequest.getUser().getAccessToken())
                .uri(URI.create(Main.comLineArgs.get("-resource") + end_URL))
                .GET()
                .build();
        return SpotifyRequest.safeSendRequest(httpRequest);
    }


    public void parseNewsFromResponse(HttpResponse<String> response) {
        PlaylistPages<MusicEntry> currentPlaylistPages = new PlaylistPages<>();
        String responseBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonElement categories = jsonObject.get("albums");
        JsonObject jsonCategoriesObject = categories.getAsJsonObject();
        JsonArray jsonArray = jsonCategoriesObject.get("items").getAsJsonArray();
        Iterator<JsonElement> categoriesIterator = jsonArray.iterator();

        for (int i = 0; i < jsonArray.size(); i++) {
            MusicEntry musicEntry = new MusicEntry();

            JsonObject element = categoriesIterator.next().getAsJsonObject();
            musicEntry.setName(element.get("name").getAsString());

            JsonArray artists = element.get("artists").getAsJsonArray();
            Iterator<JsonElement> artist = artists.iterator();
            StringBuilder string = new StringBuilder();
            string.append("[").append(artist.next().getAsJsonObject().get("name").getAsString());
            for (int j = 0; j < artists.size() - 1; j++) {
                JsonObject name = artist.next().getAsJsonObject();
                string.append(", ").append(name.get("name").getAsString());
            }
            string.append("]");
            musicEntry.setArtist(string.toString());

            JsonObject externalURLs = element.get("external_urls").getAsJsonObject();
            musicEntry.setReference(externalURLs.get("spotify").getAsString() + "\n");

            currentPlaylistPages.addMusicEntry(musicEntry);
        }

        Main.currentPages = currentPlaylistPages;
    }

}