package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


public class ServerApp {
    public static String accountsURL;
    public static int port;
    public static InetSocketAddress serverAddress;
    private static HttpServer server;

    static {
        port = 8080;
        try {
            serverAddress = new InetSocketAddress(InetAddress.getByName("localhost").getHostAddress(), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverInit();
    }

    private static void serverInit() {
        try {
            server = HttpServer.create();
            server.bind(serverAddress, 0);
            server.createContext("/", new MyHttpHandler());
            server.setExecutor(null);

        } catch (IOException e) {
            System.out.println("Server initialisation error");
            e.printStackTrace();
        }
    }

    public static String getServerAddress() {
        // http://localhost:8080
        return "http://" + "localhost" + ":" + serverAddress.getPort();
    }

    public static void setAccountsURL(String url) {
        accountsURL = url;
    }

//    public static void main(String[] args) {
//        ServerApp.start();
//    }

    public static void start() {
        System.out.println("server start");
        server.start();
    }

    public static void stop(int time) {
        server.stop(time);
    }

    public static int getPort() {
        return port;
    }


    private static class MyHttpHandler implements HttpHandler {


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String result = "Authorization code not found. Try again.";

            boolean error = exchange.getRequestURI().getQuery() == null || !exchange.getRequestURI().getQuery().startsWith("code=");

            if (!error) {
                result = "Got the code. Return back to your program.";
            }

            exchange.sendResponseHeaders(200, result.length());  // отправить headers
            exchange.getResponseBody().write(result.getBytes());  // вприсать в тело байты
            exchange.getResponseBody().close(); // закрыть тело
            if (error) {
                ServerDataStore.spotifyResponseQuery = " ";
            } else {
                ServerDataStore.spotifyResponseQuery = exchange.getRequestURI().getQuery();
            }

        }
    }
}

class ServerDataStore {
    static public String spotifyResponseQuery = " ";

    public static synchronized String getSpotifyResponseQuery() {
        return spotifyResponseQuery;
    }
}