package advisor;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRequest {

    String request;
    String[] args;
    SpotifyUser user;
    Map<String, String> currentCategories = new Hashtable<>();
    int pageShift = 0;

    public UserRequest(SpotifyUser user) {
        this.user = user;
    }

    public void parseRequest(String text) {
        String[] args = requestSeparateArgs(text);
        this.request = args[0];
        if (args.length > 1) {
            this.args = Arrays.copyOfRange(args, 1, args.length);
        }
    }

    public String[] requestSeparateArgs(String text) {
        String[] args = text.split(" ");
        if (args[0].equals("playlists") && args.length > 2) {
            args[1] = Arrays.stream(args)
                    .filter(word -> !word.equals("playlists"))
                    .collect(Collectors.joining(" "));
        }
        return args;
    }

    public String getRequest() {
        return request;
    }

    public boolean isAuthorised() {
        return user.isAuthorised();
    }

    public String[] getArgs() {
        return args;
    }

    public SpotifyUser getUser() {
        return user;
    }

    public void setUser(SpotifyUser user) {
        this.user = user;
    }

    public Map<String, String> getCurrentCategories() {
        return currentCategories;
    }

    public void setCurrentCategories(Map<String, String> currentCategories) {
        this.currentCategories = currentCategories;
    }

    public int getPageShift() {
        return pageShift;
    }

    public void setPageShift(int pageShift) {
        this.pageShift = pageShift;
    }
}