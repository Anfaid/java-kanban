package service;

import java.net.URI;
import java.net.URISyntaxException;

import Server.KVServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Manager {


    public static TaskManager getDefaultInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }



    public static TaskManager getDefault() throws ManagerSaveException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static URI DEFAULT_URI;

    static {
        try {
            DEFAULT_URI = new URI("http://localhost:" + KVServer.PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }




    public static Gson getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();

    }


}

