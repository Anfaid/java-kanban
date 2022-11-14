import model.Status;
import model.Task;
import model.SubTask;
import model.EpicTask;
import service.Manager;
import service.TaskManager;

import java.util.LinkedList;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Manager.getDefaultInMemoryTaskManager(Manager.getDefaultHistory());
        System.out.println("*** Создание всех типов задач!***");
        taskManager.createNewCommonTask(new Task("Зание 1", "Описание 1", Status.NEW));
        taskManager.createNewCommonTask(new Task("Задание 2", "Описание 2", Status.IN_PROGRESS));
        taskManager.createNewEpicTask(new EpicTask("Эпик 1", "Описание эпика 1", Status.NEW));
        taskManager.createNewEpicTask(new EpicTask("Эпик 2", "Описание эпика 2", Status.NEW));
        taskManager.createNewSubTask(new SubTask("Сабтаск 1", "Описания 1",
                Status.IN_PROGRESS, 3));
        taskManager.createNewSubTask(new SubTask("Сабтаск 2", "Описания 2",
                Status.IN_PROGRESS, 3));
        taskManager.createNewSubTask(new SubTask("Сабтаск 3", "Описания 3",
                Status.DONE, 3));

        System.out.println("*** Реализация метода истории вызовов ***");
        taskManager.getCommonTaskById(0);
        taskManager.getCommonTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getEpicTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);

        LinkedList<Task> historyList = (LinkedList<Task>) taskManager.getHistory();
        System.out.println(historyList);
    }
}

