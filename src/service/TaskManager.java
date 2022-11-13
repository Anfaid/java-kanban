package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;


public interface TaskManager {
    public int generatingId();

    public void createNewCommonTask(Task task);

    public void updateCommonTask(Task task);

    public void createNewSubTask(SubTask subTask);

    public void updateSubtask(SubTask subTask);

    public void createNewEpicTask(EpicTask epicTask);

    public void updateEpicTask(EpicTask epicTask);

    public void deleteAllCommonTasks();

    public void deleteAllSubTasks();

    public void deleteAllEpicTasks();

    public Task getCommonTaskById(int id);

    public SubTask getSubTaskById(int id);

    public EpicTask getEpicTaskById(int id);

    public void deleteCommonTaskById(int id);

    public void deleteSubTaskById(int id);

    public void deleteEpicTaskById(int id);

    public List<SubTask> getListOfSubTaskByCurEpic(int id);

    public List<Task> getListAllCommonTasks();

    public List<SubTask> getListAllSubTasks();

    public List<EpicTask> getListAllEpicTasks();

    public void checkEpicStatus(EpicTask epic);

    List<Task> getHistory();
}

