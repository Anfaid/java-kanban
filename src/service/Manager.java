package service;

import java.net.URI;
import java.net.URISyntaxException;

import Server.KVServer;

public class Manager {
    private final static TaskManager defaultManager=new InMemoryTaskManager();
    public static TaskManager getDefaultInMemoryTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager();
    }

        public static HistoryManager getDefaultHistory() {
            return new InMemoryHistoryManager();
        }

    public static TaskManager getDefault() {
        return defaultManager;
    }

    public static URI DEFAULT_URI;

    static {
        try {
            DEFAULT_URI = new URI("http://localhost:"+ KVServer.PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private static TaskManager taskManager;

    static {
        try {
            taskManager = new HttpTaskManager(DEFAULT_URI);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    }

