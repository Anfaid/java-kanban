package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.Status;

import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
     HistoryManager historyManager;
    int idOfTask = 0;
    HashMap<Integer, Task> mapOfDataTask = new HashMap<>();
    HashMap<Integer, SubTask> mapOfDataSubTask = new HashMap<>();
    HashMap<Integer, EpicTask> mapOfDataEpicTask = new HashMap<>();




    @Override

    public int generatingId() {
        return ++idOfTask;
    }

    @Override
    public void createNewCommonTask(Task task) throws ManagerSaveException {
        int taskId = generatingId();
        task.setTaskId(taskId);
        mapOfDataTask.put(taskId, task);
    }

    @Override
    public void updateCommonTask(Task task) throws ManagerSaveException {
        if (mapOfDataTask.containsKey(task.getTaskId())) {
            mapOfDataTask.put(task.getTaskId(), task);
        } else {
            System.out.println("Обычного такска с Id(" + task.getTaskId() + ") - ненайдено!");
        }
    }

    @Override
    public void createNewSubTask(SubTask subTask) throws ManagerSaveException {
        int taskId = generatingId();
        subTask.setTaskId(taskId);
        EpicTask epicTask = mapOfDataEpicTask.get(subTask.getIdOfEpicTask());
        if (epicTask != null) {
            mapOfDataSubTask.put(taskId, subTask);
            epicTask.setSubTaskId(taskId);
            checkEpicStatus(epicTask);
        } else {
            System.out.println("Эпика с ID(" + taskId + ") - ненайдено");
        }

    }

    @Override
    public void updateSubtask(SubTask subTask) throws ManagerSaveException {
        if (mapOfDataSubTask.containsKey(subTask.getTaskId())) {
            mapOfDataSubTask.put(subTask.getTaskId(), subTask);
            EpicTask epicTask = mapOfDataEpicTask.get(subTask.getIdOfEpicTask());
            checkEpicStatus(epicTask);
        } else {
            System.out.println("Сабтаска с ID(" + subTask.getTaskId() + ") - ненайдено");
        }
    }

    @Override
    public void createNewEpicTask(EpicTask epicTask) throws ManagerSaveException {
        int taskId = generatingId();
        epicTask.setTaskId(taskId);
        mapOfDataEpicTask.put(taskId, epicTask);
        checkEpicStatus(epicTask);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {
        if (mapOfDataEpicTask.containsKey(epicTask.getTaskId())) {
            mapOfDataEpicTask.put(epicTask.getTaskId(), epicTask);
            checkEpicStatus(epicTask);
        }
    }

    @Override
    public void deleteAllCommonTasks() throws ManagerSaveException {
        for (Integer key : mapOfDataTask.keySet()) {
            historyManager.remove(key);
        }

        mapOfDataTask.clear();
    }

    @Override
    public void deleteAllSubTasks() throws ManagerSaveException {
        for (Integer key : mapOfDataSubTask.keySet()) {
            historyManager.remove(key);
        }

        mapOfDataSubTask.clear();

        if (mapOfDataSubTask.isEmpty()) {
            for (EpicTask epic : mapOfDataEpicTask.values()) {
                epic.getListOfSubTask().clear();
                checkEpicStatus(epic);

            }
        }
    }

    @Override
    public void deleteAllEpicTasks() throws ManagerSaveException {
        for (Integer key : mapOfDataSubTask.keySet()) {
            historyManager.remove(key);
        }
        for (Integer key : mapOfDataEpicTask.keySet()) {
            historyManager.remove(key);
        }

        mapOfDataEpicTask.clear();
        mapOfDataSubTask.clear();
    }

    @Override
    public Task getCommonTaskById(int id) throws ManagerSaveException {
        historyManager.addLast(mapOfDataTask.get(id));
        return mapOfDataTask.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) throws ManagerSaveException {
        historyManager.addLast(mapOfDataSubTask.get(id));
        return mapOfDataSubTask.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) throws ManagerSaveException {
        historyManager.addLast(mapOfDataEpicTask.get(id));
        return mapOfDataEpicTask.get(id);
    }

    @Override
    public void deleteCommonTaskById(int id) throws ManagerSaveException {
        if (mapOfDataTask.containsKey(id)) {
            mapOfDataTask.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Обычного такска с Id(" + id + ") - ненайдено!");
        }

    }

    @Override
    public void deleteSubTaskById(int id) throws ManagerSaveException {
        if (mapOfDataSubTask.containsKey(id)) {
            mapOfDataSubTask.remove(id);
            historyManager.remove(id);
            if (mapOfDataSubTask.get(id) == null) {
                for (EpicTask epic : mapOfDataEpicTask.values()) {
                    if (epic.getListOfSubTask().contains(id)) {
                        epic.getListOfSubTask().remove(id);
                        checkEpicStatus(epic);
                    }
                }
            }
        } else {
            System.out.println("Сабтаска с ID(" + id + ") - ненайдено");
        }
    }

    @Override
    public void deleteEpicTaskById(int id) throws ManagerSaveException {
        if (mapOfDataEpicTask.containsKey(id)) {
            EpicTask epic = mapOfDataEpicTask.get(id);
            for (Integer idOfSubTask : epic.getListOfSubTask()) {
                mapOfDataSubTask.remove(idOfSubTask);
                historyManager.remove(idOfSubTask);
            }
            mapOfDataEpicTask.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Эпика с ID(" + id + ") - ненайдено");
        }

    }

    @Override
    public List<SubTask> getListOfSubTaskByCurEpic(int id) {
        if (mapOfDataEpicTask.containsKey(id)) {
            EpicTask epic = mapOfDataEpicTask.get(id);
            List<SubTask> listOfCurEpic = new ArrayList<>();
            for (int i = 0; i < epic.getListOfSubTask().size(); i++) {
                listOfCurEpic.add(mapOfDataSubTask.get(epic.getListOfSubTask().get(i)));
            }
            return listOfCurEpic;
        } else {
            System.out.println("Эпика с ID(" + id + ") - ненайдено");
            return Collections.emptyList();
        }
    }

    @Override
    public List<Task> getListAllCommonTasks() throws ManagerSaveException {
        if (!mapOfDataTask.isEmpty()) {
            return new ArrayList<>(mapOfDataTask.values());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<SubTask> getListAllSubTasks() throws ManagerSaveException {
        if (!mapOfDataTask.isEmpty()) {
            return new ArrayList<>(mapOfDataSubTask.values());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<EpicTask> getListAllEpicTasks() throws ManagerSaveException {
        if (!mapOfDataTask.isEmpty()) {
            return new ArrayList<>(mapOfDataEpicTask.values());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void checkEpicStatus(EpicTask epic) {
        if (mapOfDataEpicTask.containsKey(epic.getTaskId())) {
            if (epic.getListOfSubTask().size() == 0) {
                epic.setTaskStatus(Status.NEW);
            }
        } else {
            List<SubTask> listOfCurEpic = new ArrayList<>();
            for (int i = 0; i < epic.getListOfSubTask().size(); i++) {
                listOfCurEpic.add(mapOfDataSubTask.get(epic.getListOfSubTask().get(i)));
            }
            boolean SubStatusNew = true;
            boolean SubStatusInProgress = true;
            boolean SubStatusDone = true;
            for (SubTask subTask : listOfCurEpic) {
                SubStatusNew &= (subTask.getTaskStatus().equals(Status.NEW));
                SubStatusInProgress &= (subTask.getTaskStatus().equals(Status.IN_PROGRESS));
                SubStatusDone &= (subTask.getTaskStatus().equals(Status.DONE));
            }
            if (SubStatusNew) {
                epic.setTaskStatus(Status.NEW);
            } else if (SubStatusDone) {
                epic.setTaskStatus((Status.DONE));
            } else {
                epic.setTaskStatus(Status.IN_PROGRESS);
            }

        }
    }

    @Override
    public List<Task> getHistory() throws ManagerSaveException {
        return historyManager.getHistory();
    }
}
