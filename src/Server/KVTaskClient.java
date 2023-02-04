package Server;

import service.ManagerSaveException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    String url;
    String apiToken;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;
        this.apiToken = register();
    }

    private String register() {
        URI uri = URI.create(url + "/register");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new ManagerSaveException("Bad response, status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException | ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + this.apiToken)).expectContinue(true)
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Bad response, status code: " + response.statusCode());
            }
            System.out.println("KV вернул " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("no tasks");
        }
    }


    public void put(String key, String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + this.apiToken))
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new ManagerSaveException("Bad response, status code: " + response.statusCode());
            }
            System.out.println("Сохранили на KV " + json);
        } catch (IOException | InterruptedException | ManagerSaveException e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.body());
    }
}
