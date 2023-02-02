package Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Server.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Server.HttpTaskServer;
import service.Manager;
import service.ManagerSaveException;
import service.TaskManager;

public class HttpTaskServerTest {
    private static final String uriBase = "http://localhost:8080";
    private static HttpClient httpClient;
    private static Gson gson;
    private static HttpTaskServer taskServer;
    private static TaskManager manager;


    HttpTaskServerTest() {
    }
    @AfterAll
    static void AfterAll() {
        try {
            taskServer.stopServer();
        } catch (Throwable e) {
            throw e;
        }
    }

    @BeforeEach
    void BeforeEach() throws IOException, ManagerSaveException {
        manager.deleteAllCommonTasks();
    }

    @BeforeAll
    static void BeforeAll() throws IOException {

        gson = new Gson();
        httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3L)).build();
        taskServer = new HttpTaskServer();
        taskServer.startServer();
        manager = Manager.getDefault();

    }

    @Test
    void testTasksHandlers() throws IOException, InterruptedException, ManagerSaveException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/tasks/")).version(Version.HTTP_1_1).build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        List<Task> tasks =  gson.fromJson(response.body(), ArrayList.class);
        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(manager.getListAllCommonTasks().size(), tasks.size());
        Task task = new Task("Task3", "Description3", Status.NEW, Instant.now(), Duration.ofMinutes(5));
        manager.createNewCommonTask(task);
        response = httpClient.send(request, BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());

        tasks =  gson.fromJson(response.body(), ArrayList.class);
        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(manager.getListAllCommonTasks().size(), tasks.size());
    }

    @Test
    void testRootHandlers() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/")).version(Version.HTTP_1_1).build();
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotEquals(0, ((String) response.body()).length());

    }

    @Test
    void testStartAndStopServer() {
        try {
            Assertions.assertDoesNotThrow(() -> {
                taskServer.stopServer();
            });
            Assertions.assertDoesNotThrow(() -> {
                taskServer.startServer();
            });
        } catch (Throwable e) {
            throw e;
        }
    }

    @Test
    void testHistoryHandlers() throws IOException, InterruptedException, ManagerSaveException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/tasks/history/"))
                .version(Version.HTTP_1_1).build();
        for (int i = 1; i <= 3; ++i) {
            Task task = new Task("Task1", "Description1",Status.NEW,
                    Instant.ofEpochMilli(45678765490l), Duration.ofMinutes(5));
            manager.createNewCommonTask(task);
            manager.getCommonTaskById(task.getTaskId());
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            Assertions.assertEquals(200, response.statusCode());
            String lastResponse = (String) response.body();
            Type listType = (new TypeToken<ArrayList<Task>>() {
            }).getType();
            List<Task> history = (List) gson.fromJson((String) response.body(), listType);
            Assertions.assertNotNull(history);
            Assertions.assertEquals(manager.getListAllCommonTasks(), history.size());
            String s = gson.toJson(task);
            Assertions.assertTrue(lastResponse.contains(s));
        }
    }

}
