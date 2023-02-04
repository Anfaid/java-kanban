package Server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.EpicTask;
import model.SubTask;
import model.Task;
import service.Manager;
import service.ManagerSaveException;
import service.TaskManager;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    public static final String ROOT_PATH = "/";
    public static final String TASKS_PATH = "/tasks/";
    public static final String TASKS_TASK_PATH = "/tasks/task/";
    public static final String TASKS_EPIC_PATH = "/tasks/epic/";
    public static final String TASKS_SUBTASK_PATH = "/tasks/subtask/";
    public static final String TASKS_HISTORY_PATH = "/tasks/history/";
    private static final Charset DEFAULT_CHARSET = null;

    private final TaskManager taskManager;
    private HttpServer httpServer;

    Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {

        this.taskManager = taskManager;
        gson = Manager.getGsonBuilder();
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        httpServer.start();
        System.out.println("HTTP-сервер запущен!");
    }

    private void handler(HttpExchange exchange) {
        try {
            System.out.println("Идет обработка запроса " + exchange.getRequestURI().getPath());
            String path = exchange.getRequestURI().getPath().substring(7);
            String method = exchange.getRequestMethod();
            final String query = exchange.getRequestURI().getQuery();
            switch (method) {
                case "GET":

                    if (path.equals("task/")) {
                        if (query == null) {
                            String response = gson.toJson(taskManager.getListAllCommonTasks());
                            responseText(exchange, response);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);
                        String response = gson.toJson(taskManager.getCommonTaskById(id));
                        responseText(exchange, response);
                        return;
                    }
                    if (path.equals("subtask/")) {
                        if (query == null) {
                            String response = gson.toJson(taskManager.getListAllSubTasks());
                            responseText(exchange, response);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);
                        String response = gson.toJson(taskManager.getSubTaskById(id));
                        responseText(exchange, response);
                        return;
                    }
                    if (path.equals("epic/")) {
                        if (query == null) {
                            Type typeOfEpic = new TypeToken<ArrayList<EpicTask>>() {
                            }.getType();
                            String response = gson.toJson(taskManager.getListAllEpicTasks(), typeOfEpic);
                            responseText(exchange, response);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);

                        String response = gson.toJson(taskManager.getEpicTaskById(id));
                        responseText(exchange, response);
                        return;
                    }
                    if (path.equals("subtask/epic/")) {
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);
                        String response = gson.toJson(taskManager.getListOfSubTaskByCurEpic(id));
                        responseText(exchange, response);
                        return;
                    }
                    if (path.equals("")) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        responseText(exchange, response);
                        return;
                    }
                    if (path.equals("history/")) {
                        String response = gson.toJson(taskManager.getHistory());
                        responseText(exchange, response);
                    }
                    break;
                case "POST":
                    if (path.equals("task/")) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskString = new String(((InputStream) inputStream).readAllBytes());
                        if (taskString.isEmpty()) {
                            writeResponse(exchange, "Поля комментария пустые!", 400);
                            return;
                        }
                        try {
                            Task task = gson.fromJson(taskString, Task.class);
                        } catch (JsonSyntaxException exception) {
                            writeResponse(exchange, "Тело некорректно, попробуйте еще раз!", 400);
                            return;
                        }
                        Task task = gson.fromJson(taskString, Task.class);
                        taskManager.createNewCommonTask(task);
                        writeResponse(exchange, "Задача успешно создана!!", 201);
                    }
                    break;
                case "DELETE":
                    if (path.equals("task/")) {
                        if (query == null) {
                            taskManager.deleteAllCommonTasks();
                            exchange.sendResponseHeaders(200, 0);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);
                        taskManager.deleteCommonTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    if (path.equals("subtask/")) {
                        if (query == null) {
                            taskManager.deleteAllSubTasks();
                            exchange.sendResponseHeaders(200, 0);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);
                        taskManager.deleteSubTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    if (path.equals("epic/")) {
                        if (query == null) {
                            taskManager.deleteAllEpicTasks();
                            exchange.sendResponseHeaders(200, 0);
                            return;
                        }
                        String idString = query.substring(3);
                        int id = decodeTaskId(idString);
                        taskManager.deleteEpicTaskById(id);
                        exchange.sendResponseHeaders(200, 0);
                        return;
                    }
                    break;
                default:
                    System.out.println("Ждем GET или DELETE запрос, а получаем" + method);
                    exchange.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            System.out.println("Произошло падение сервера");
            exchange.close();
        } finally {
            exchange.close();
        }
    }

    private static int decodeTaskId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void stopServer() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен!");
    }

    private void responseText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
    }

    private static void writeResponse(HttpExchange exchange, String responseString,
                                      int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0L);
            OutputStream os = exchange.getResponseBody();
            try {
                os.write(String.valueOf(responseCode).getBytes(DEFAULT_CHARSET));
            } catch (Throwable ex1) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Throwable ex2) {
                        ex1.addSuppressed(ex2);
                    }
                }
                throw ex1;
            }
            if (os != null) {
                os.close();
            }
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, (long) bytes.length);
            OutputStream os = exchange.getResponseBody();
            try {
                os.write(bytes);
            } catch (Throwable ex3) {
                if (os != null) {
                    try {
                        os.close();
                    } catch (Throwable var7) {
                        ex3.addSuppressed(var7);
                    }
                }
                throw ex3;
            }
            if (os != null) {
                os.close();
            }
        }
        exchange.close();
    }
}

    




