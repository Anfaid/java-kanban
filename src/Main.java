import model.Status;
import model.Task;
import model.SubTask;
import model.EpicTask;
import service.Manager;

import java.util.List;


public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.createNewCommonTask(new Task("Зание 1", "Описание 1", Status.NEW));
        manager.createNewCommonTask(new Task("Задание 2", "Описание 2", Status.IN_PROGRESS));
        System.out.println("*********");

        List<Task> ListOfTask = manager.getListAllCommonTasks();
        System.out.println(ListOfTask);
        Task task = manager.getCommonTaskById(1);
        task.setTaskStatus(Status.DONE);
        manager.updateCommonTask(task);
        System.out.println(task);
        System.out.println("*********");

        manager.createNewEpicTask(new EpicTask("Эпик 1", "Описание эпика 1", Status.NEW));
        manager.createNewEpicTask(new EpicTask("Эпик 2", "Описание эпика 2", Status.NEW));
        List<EpicTask> ListOfEpicTask = manager.getListAllEpicTasks();
        System.out.println(ListOfEpicTask);
        EpicTask epic = manager.getEpicTaskById(4);
        epic.setTaskStatus(Status.IN_PROGRESS);
        manager.updateEpicTask(epic);
        EpicTask epicTask = manager.getEpicTaskById(4);
        System.out.println(epicTask);
        System.out.println("*********");

        manager.createNewSubTask(new SubTask("Сабтаск 1", "Описания 1",
                Status.IN_PROGRESS, 3));
        manager.createNewSubTask(new SubTask("Сабтаск 2", "Описания 2",
                Status.IN_PROGRESS, 3));
        manager.createNewSubTask(new SubTask("Сабтаск 3", "Описания 3",
                Status.DONE, 3));
        List<SubTask> ListOfSubTask = manager.getListOfSubTaskByCurEpic(3);
        System.out.println(ListOfSubTask);
        SubTask subTask = manager.getSubTaskById(5);
        subTask.setTaskStatus(Status.DONE);
        manager.updateSubtask(subTask);
        System.out.println(subTask);
        System.out.println("*********");

        manager.deleteCommonTaskById(1);
        List<Task> ListOfTask1 = manager.getListAllCommonTasks();
        System.out.println(ListOfTask1);
        manager.deleteEpicTaskById(3);
        List<EpicTask> ListOfEpicTask1 = manager.getListAllEpicTasks();
        System.out.println(ListOfEpicTask1);
    }

}
