import model.Status;
import model.Task;
import model.SubTask;
import model.EpicTask;
import service.FileBackedTasksManager;
import service.Manager;
import service.ManagerSaveException;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        File newFile = new File("practicum.csv");



        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(newFile);


    }
}

