package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.*;


public interface TaskManager {
    public int generatingId();

    public void createNewCommonTask(Task task) throws IOException;

    public void updateCommonTask(Task task);

    public void createNewSubTask(SubTask subTask) throws IOException;

    public void updateSubtask(SubTask subTask);

    public void createNewEpicTask(EpicTask epicTask) throws IOException;

    public void updateEpicTask(EpicTask epicTask);

    public void deleteAllCommonTasks();

    public void deleteAllSubTasks();

    public void deleteAllEpicTasks();

    public Task getCommonTaskById(int id) throws IOException;

    public SubTask getSubTaskById(int id) throws IOException;

    public EpicTask getEpicTaskById(int id) throws IOException;

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

