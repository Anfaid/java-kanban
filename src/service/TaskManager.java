package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.util.*;


public interface TaskManager {
    public int generatingId();

    public void createNewCommonTask(Task task) throws IOException, ManagerSaveException;

    public void updateCommonTask(Task task) throws IOException, ManagerSaveException;

    public void createNewSubTask(SubTask subTask) throws IOException, ManagerSaveException;

    public void updateSubtask(SubTask subTask) throws IOException, ManagerSaveException;

    public void createNewEpicTask(EpicTask epicTask) throws IOException, ManagerSaveException;

    public void updateEpicTask(EpicTask epicTask) throws IOException, ManagerSaveException;

    public void deleteAllCommonTasks() throws IOException, ManagerSaveException;

    public void deleteAllSubTasks() throws IOException, ManagerSaveException;

    public void deleteAllEpicTasks() throws IOException, ManagerSaveException;

    public Task getCommonTaskById(int id) throws IOException, ManagerSaveException;

    public SubTask getSubTaskById(int id) throws IOException, ManagerSaveException;

    public EpicTask getEpicTaskById(int id) throws IOException, ManagerSaveException;

    public void deleteCommonTaskById(int id) throws IOException, ManagerSaveException;

    public void deleteSubTaskById(int id) throws IOException, ManagerSaveException;

    public void deleteEpicTaskById(int id) throws IOException, ManagerSaveException;

    public List<SubTask> getListOfSubTaskByCurEpic(int id);

    public List<Task> getListAllCommonTasks() throws ManagerSaveException;

    public List<SubTask> getListAllSubTasks() throws ManagerSaveException;

    public List<EpicTask> getListAllEpicTasks() throws ManagerSaveException;

    public void checkEpicStatus(EpicTask epic);

    List<Task> getHistory() throws ManagerSaveException;

    public HashMap<Integer, EpicTask> getEpicMap();
    public HashMap<Integer, Task> getTaskMap();
    public HashMap<Integer, SubTask> getSubtaskMap();

    public List<Task> getPrioritizedTasks();
}

