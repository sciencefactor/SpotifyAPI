package advisor;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// https://accounts.spotify.com/authorize?client_id=bf6c46c9c94149abbccd6dc698518908&redirect_uri=http://localhost:8080&response_type=code

public class SpotifyRequest {
    private static String serverAddress;


    public static void setServerAddress(String address) {
        serverAddress = address;
    }


//    public static void main(String[] args) {
//
//        HttpResponse<String> response = sendGetMethod();
//        System.out.println("body = " + response.body() + ", statusCode = " + response.statusCode() + ", headers = " + response.headers() + ", uri = " + response.uri());
//        response = sendPostMethod("login=admin&password=admin");
//        System.out.println("body = " + response.body() + ", statusCode = " + response.statusCode() + ", headers = " + response.headers() + ", uri = " + response.uri());
//
//    }

    public static HttpResponse<String> sendGetMethod() {
        HttpRequest httpGetRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(serverAddress))
                .GET()
                .build();
        return safeSendRequest(httpGetRequest);
    }

    public static HttpResponse<String> sendPostMethod(String body) {
        HttpRequest httpPostRequest = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(serverAddress))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return safeSendRequest(httpPostRequest);
    }

    public static HttpResponse<String> safeSendRequest(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = Main.client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error during request sending");
            e.printStackTrace();
        }
        return response;
    }

    public static void errorPrint(HttpResponse<String> response){
        String responseBody = response.body();
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonElement categories = jsonObject.get("error");
        JsonObject jsonCategoriesObject = categories.getAsJsonObject();
        String errorMessage = jsonCategoriesObject.get("message").getAsString();
        System.out.println(errorMessage);
    }
}