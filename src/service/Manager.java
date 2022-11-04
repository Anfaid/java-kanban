package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;


public class Manager {
    int idOfTask = 0;
    HashMap<Integer, Task> mapOfDataTask = new HashMap<>();
    HashMap<Integer, SubTask> mapOfDataSubTask = new HashMap<>();
    HashMap<Integer, EpicTask> mapOfDataEpicTask = new HashMap<>();

    public int generatingId() {
        return ++idOfTask;
    }

    public void createNewCommonTask(Task task) {
        int taskId = generatingId();
        task.setTaskId(taskId);
        mapOfDataTask.put(taskId, task);
    }

    public void updateCommonTask(Task task) {
        if (mapOfDataTask.containsKey(task.getTaskId())) {
            mapOfDataTask.put(task.getTaskId(), task);
        } else {
            System.out.println("Обычного такска с Id(" + task.getTaskId() + ") - ненайдено!");
        }
    }

    public void createNewSubTask(SubTask subTask) {
        int taskId = generatingId();
        subTask.setTaskId(taskId);
        EpicTask epicTask = mapOfDataEpicTask.get(subTask.getIdOfEpicTask());
        if (epicTask != null) {
            mapOfDataSubTask.put(taskId, subTask);
            epicTask.setSubTaskId(taskId);
        } else {
            System.out.println("Эпика с ID(" + taskId + ") - ненайдено");
        }

    }

    public void updateSubtask(SubTask subTask) {
        if (mapOfDataSubTask.containsKey(subTask.getTaskId())) {
            mapOfDataSubTask.put(subTask.getTaskId(), subTask);
            EpicTask epicTask = mapOfDataEpicTask.get(subTask.getIdOfEpicTask());
        } else {
            System.out.println("Сабтаска с ID(" + subTask.getTaskId() + ") - ненайдено");
        }
    }

    public void createNewEpicTask(EpicTask epicTask) {
        int taskId = generatingId();
        epicTask.setTaskId(taskId);
        mapOfDataEpicTask.put(taskId, epicTask);
        checkEpicStatus(epicTask);
    }

    public void updateEpicTask(EpicTask epicTask) {
        if (mapOfDataEpicTask.containsKey(epicTask.getTaskId())) {
            mapOfDataEpicTask.put(epicTask.getTaskId(), epicTask);
            checkEpicStatus(epicTask);
        }
    }

    public void deleteAllCommonTasks() {
        mapOfDataTask.clear();
    }

    public void deleteAllSubTasks() {
        mapOfDataSubTask.clear();

        if (mapOfDataSubTask.isEmpty()) {
            for (EpicTask epic : mapOfDataEpicTask.values()) {
                epic.getListOfSubTask().clear();

            }
        }
    }

    public void deleteAllEpicTasks() {
        mapOfDataEpicTask.clear();
        mapOfDataSubTask.clear();
    }

    public Task getCommonTaskById(int id) {
        return mapOfDataTask.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return mapOfDataSubTask.get(id);
    }

    public EpicTask getEpicTaskById(int id) {
        return mapOfDataEpicTask.get(id);
    }

    public void deleteCommonTaskById(int id) {
        if (mapOfDataTask.containsKey(id)) {
            mapOfDataTask.remove(id);
        } else {
            System.out.println("Обычного такска с Id(" + id + ") - ненайдено!");
        }

    }

    public void deleteSubTaskById(int id) {
        if (mapOfDataSubTask.containsKey(id)) {
            mapOfDataSubTask.remove(id);
            if (mapOfDataSubTask.get(id) == null) {
                for (EpicTask epic : mapOfDataEpicTask.values()) {
                    if (epic.getListOfSubTask().contains(id)) {
                        epic.getListOfSubTask().remove(id);
                    }
                }
            }
        } else {
            System.out.println("Сабтаска с ID(" + id + ") - ненайдено");
        }
    }

    public void deleteEpicTaskById(int id) {
        if (mapOfDataEpicTask.containsKey(id)) {
            EpicTask epic = mapOfDataEpicTask.get(id);
            for (Integer idOfSubTask : epic.getListOfSubTask()) {
                mapOfDataSubTask.remove(idOfSubTask);
            }
            mapOfDataEpicTask.remove(id);
        } else {
            System.out.println("Эпика с ID(" + id + ") - ненайдено");
        }

    }

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

    public List<Task> getListAllCommonTasks() {
        if (!mapOfDataTask.isEmpty()) {
            return new ArrayList<>(mapOfDataTask.values());
        } else {
            return Collections.emptyList();
        }
    }

    public List<SubTask> getListAllSubTasks() {
        if (!mapOfDataTask.isEmpty()) {
            return new ArrayList<>(mapOfDataSubTask.values());
        } else {
            return Collections.emptyList();
        }
    }

    public List<EpicTask> getListAllEpicTasks() {
        if (!mapOfDataTask.isEmpty()) {
            return new ArrayList<>(mapOfDataEpicTask.values());
        } else {
            return Collections.emptyList();
        }
    }

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
            } else if (SubStatusInProgress) {
                epic.setTaskStatus(Status.IN_PROGRESS);
            } else {
                epic.setTaskStatus((Status.DONE));
            }

        }
    }
}

