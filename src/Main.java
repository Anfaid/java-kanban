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
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(newFile);

        fileBackedTasksManager.createNewCommonTask(new Task("Task1","Task1", Status.NEW));
        fileBackedTasksManager.createNewCommonTask(new Task("Task2","Task2", Status.DONE));
        fileBackedTasksManager.createNewEpicTask(new EpicTask("Epic1", "Epic1", Status.NEW));
        fileBackedTasksManager.createNewSubTask(new SubTask("Subtask 1", "Subtask 1",
                Status.NEW, 3));
        fileBackedTasksManager.getEpicTaskById(3);
        fileBackedTasksManager.getCommonTaskById(1);
        fileBackedTasksManager.getCommonTaskById(2);
        fileBackedTasksManager.getSubTaskById(4);
        fileBackedTasksManager.getListAllCommonTasks();
        fileBackedTasksManager.getListAllSubTasks();
        fileBackedTasksManager.getListAllEpicTasks();
        fileBackedTasksManager.getHistory();


        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(newFile);


    }
}

