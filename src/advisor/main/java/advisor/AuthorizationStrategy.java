package advisor;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;

public class AuthorizationStrategy extends ReactionStrategies {
    public static String client_id = "bf6c46c9c94149abbccd6dc698518908";
    private final String end_URL = "/api/token";

    @Override
    UserReply produce(UserRequest userRequest) {
        return makeUserAuthorisation(userRequest);
    }

    private UserReply makeUserAuthorisation(UserRequest userRequest) {
        System.out.println("use this link to request the access code:");
        System.out.println(generateAuthAddress());
        getAuthorizationCode(userRequest);
        getAccessToken(userRequest);
        System.out.println("---SUCCESS---");
        return new UserReply();
    }

    private String generateAuthAddress() {
        StringBuilder address = new StringBuilder();
        address.append(Main.comLineArgs.get("-access"))
                .append("/authorize?")
                .append("client_id=").append(client_id).append("&")
                .append("redirect_uri=")
                .append("http://localhost:8080")
                .append("&")
                .append("response_type=code");
        return address.toString();
    }

    private void getAuthorizationCode(UserRequest userRequest) {
        ServerApp.start();
        System.out.println("waiting for code...");

        while (true) {
            waitForResponse();
            if (!ServerDataStore.getSpotifyResponseQuery().equals(" ")) {
                userRequest.getUser().setAuthorizationCode(ServerDataStore.getSpotifyResponseQuery());
                System.out.println("code received");
                break;
            }
        }
        ServerApp.stop(1);
    }

    private void waitForResponse() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getAccessToken(UserRequest userRequest) {
        System.out.println("making http request for access_token...");
        String authServer = Main.comLineArgs.get("-access") + end_URL;
        SpotifyRequest.setServerAddress(authServer);

        String authorizationCode = userRequest.getUser().getAuthorizationCode();
        String postBody = generatePostBody(authorizationCode);
        HttpResponse<String> response = SpotifyRequest.sendPostMethod(postBody);
        System.out.println("response:");
        System.out.println(response.body());
        JsonObject jsonResponse = convertResponseToJson(response);

        userRequest.getUser().setAccessToken(jsonResponse.get("access_token").getAsString());
        userRequest.getUser().setAuthorised();
    }

    private String generatePostBody(String authorizationCode) {
        StringBuilder postBody = new StringBuilder();
        postBody.append("grant_type=").append("authorization_code&")
                .append("client_id=").append(client_id).append("&")
                .append("client_secret=").append("7d26926024954892a094e6bb2056c68b").append("&")
                .append(authorizationCode).append("&")
                .append("redirect_uri=").append(ServerApp.getServerAddress()).append("&");

        return postBody.toString();
    }

    private JsonObject convertResponseToJson(HttpResponse<String> response) {
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }

}