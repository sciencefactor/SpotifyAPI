package advisor;

import java.util.Scanner;

public class SpotifyUser {
    private static int globalID = 0;
    private final String name;
    private final int userID;
    private boolean authorisation = false;
    private final Scanner scanner;
    private final ReactionContext reactionContext;
    private final UserRequest userRequest = new UserRequest(this);
    private String authorizationCode;
    private String accessToken;

    public SpotifyUser(String name) {
        reactionContext = new ReactionContext();
        this.scanner = new Scanner(System.in);
        this.name = name;
        this.userID = globalID;
        globalID++;
    }

    public static int getGlobalID() {
        return globalID;
    }

    public void startSession() {
        String userInput;
        UserReply reply;

        while (scanner.hasNext()) {
            userInput = scanner.nextLine();
            userRequest.parseRequest(userInput);
            reactionContext.setMethod(userRequest);
            reply = reactionContext.produce(userRequest);
            if (reply.getReply().equals("exit")) {
                break;
            }
        }
    }

    public void setAuthorised() {
        this.authorisation = true;
    }

    public boolean isAuthorised() {
        return authorisation;
    }

    public String getName() {
        return name;
    }

    public int getUserID() {
        return userID;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String code) {
        this.authorizationCode = code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}