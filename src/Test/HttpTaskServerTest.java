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
import java.util.Map;

import Server.HttpTaskServer;
import Server.KVServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;
import Server.HttpTaskServer;
import service.Manager;
import service.ManagerSaveException;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    private static final String uriBase = "http://localhost:8080";
    private HttpClient client = HttpClient.newHttpClient();
    private static Gson gson;
    private static HttpTaskServer taskServer;
    private static TaskManager manager;
    private static KVServer kvServer;


    HttpTaskServerTest() {
    }

    protected void Tasks() throws IOException, ManagerSaveException{
        Task task1 = new Task("Task1", "Description1",Status.NEW,
Instant.ofEpochMilli(45678765490l), Duration.ofMinutes(5));
        manager.createNewCommonTask(task1);
        Task task2 = new Task("Task2", "Description3",Status.NEW,
                Instant.ofEpochMilli(45678765490l), Duration.ofMinutes(5));
        manager.createNewCommonTask(task2);
        EpicTask epic = new EpicTask("epic1", "Description1",Status.NEW,
                Instant.ofEpochMilli(45678765490l), Duration.ofMinutes(5));
        manager.createNewEpicTask(epic);
        SubTask subTask1 = new SubTask("st1", "Description1",Status.NEW, epic.getTaskId(),
                Instant.ofEpochMilli(45678765490l), Duration.ofMinutes(5));
        manager.createNewSubTask(subTask1);
        SubTask subTask2 = new SubTask("st2", "Description1",Status.NEW, epic.getTaskId(),
                Instant.ofEpochMilli(45678765490l), Duration.ofMinutes(5));
        manager.createNewSubTask(subTask2);

    }
    @AfterEach
   void AfterEach() {
        taskServer.stopServer();
        kvServer.stop();
    }

    @BeforeEach
    void BeforeEach() throws IOException, ManagerSaveException {
        Tasks();
        manager = Manager.getDefaultInMemoryTaskManager();
        kvServer = new KVServer();
        kvServer.start();
        gson = new Gson();
        taskServer = new HttpTaskServer(manager);
    }






}
