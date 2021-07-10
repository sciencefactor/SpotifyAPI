package advisor;

import java.net.http.HttpClient;
import java.util.HashMap;

public class Main {
    public static HashMap<String, String> comLineArgs = new HashMap<>();
    public static HttpClient client = HttpClient.newBuilder().build();
    public static PlaylistPages currentPages;

    //TODO удалить clientID

    public static void main(String[] args) {
        separateComLineArgs(args);
        SpotifyUser user = new SpotifyUser("John");
        user.startSession();

    }

    static void separateComLineArgs(String[] strings) {
        if (strings.length > 0) {
            for (int i = 0; i < strings.length - 1; i++) {
                if (strings[i].startsWith("-")) {
                    comLineArgs.put(strings[i], strings[i + 1]);
                }
            }
        } else {
            comLineArgs.put("-access", "https://accounts.spotify.com");
            comLineArgs.put("-resource", "https://api.spotify.com");
            comLineArgs.put("-page", "5");
        }
    }

    static int getPort(String addressPort) {
        String[] separate = addressPort.split(":");
        return Integer.parseInt(separate[1]);
    }

    static String getAddress(String addressPort) {
        String[] separate = addressPort.split(":");
        return separate[0];
    }
}