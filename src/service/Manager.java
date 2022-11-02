package service;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    Integer idOfTask = 0;
    HashMap<Integer, Task> mapOfDataTask = new HashMap<>();
    HashMap<Integer, SubTask> mapOfDataSubTask = new HashMap<>();
    HashMap<Integer, EpicTask> mapOfDataEpicTask = new HashMap<>();

    public ArrayList<Task> getListOfAllTask() {
        ArrayList<Task> ListAllOfTasks = new ArrayList<>();
        System.out.println("Список всех базовых задач: ");
        if (!mapOfDataTask.isEmpty()) {
            for (Integer idOfTask : mapOfDataTask.keySet()) {
                System.out.println("TaskID- " + idOfTask + " .Задача: " + mapOfDataTask.get(idOfTask));
                ListAllOfTasks.add(mapOfDataTask.get(idOfTask));
            }
        } else {
            System.out.println("Список базовых задач пуст!");
        }
        System.out.println("Список всех подзадач: ");
        if (!mapOfDataSubTask.isEmpty()) {
            for (Integer idOfTask : mapOfDataSubTask.keySet()) {
                System.out.println("TaskID- " + idOfTask + " .Подзадачи: " + mapOfDataSubTask.get(idOfTask));
                ListAllOfTasks.add(mapOfDataSubTask.get(idOfTask));
            }
        } else {
            System.out.println("Список подзадач пуст!");
        }
        System.out.println("Список всех Эпиков: ");
        if (!mapOfDataEpicTask.isEmpty()) {
            for (Integer idOfTask : mapOfDataEpicTask.keySet()) {
                System.out.println("TaskID- " + idOfTask + " .Подзадачи: " + mapOfDataEpicTask.get(idOfTask));
                ListAllOfTasks.add(mapOfDataEpicTask.get(idOfTask));
            }
        }
        return ListAllOfTasks;
    }

    public void removeAllTask() {
        mapOfDataEpicTask.clear();
        mapOfDataTask.clear();
        mapOfDataSubTask.clear();
    }

    public Task gettingTaskById(Integer inputId) {
        Task curTask = null;
        if (mapOfDataTask.containsKey(inputId)) {
            curTask = mapOfDataTask.get(inputId);
        } else if (mapOfDataSubTask.containsKey(inputId)) {
            curTask = mapOfDataSubTask.get(inputId);
        } else if (mapOfDataEpicTask.containsKey(inputId)) {
            curTask = mapOfDataEpicTask.get(inputId);
        } else {
            System.out.println("Задачи с iD(" + inputId + ") несуществует!");
        }
        return curTask;

    }

    public void createOrUpdateCommonTask(Integer id, String nameOfTask, String taskDescription, Status status) {

        if (mapOfDataTask.containsKey(id)) {
            mapOfDataTask.remove(id);
            Task updateTask = new Task(id, nameOfTask, taskDescription, status);
            mapOfDataTask.put(id, updateTask);


        } else if (id == null) { // Если пользователь не передал iD для задачи
            idOfTask ++;
            Task newTask = new Task(idOfTask, nameOfTask, taskDescription, status);
            newTask.setTaskId(idOfTask);
            mapOfDataTask.put(idOfTask, newTask);
        } else {
            System.out.println("Задача - " + nameOfTask + " не была инициализирована! " + "\n" +
                    "Задачи №" + id + " несуществует в базе данных!");
        }
    }


    public void createOrUpdateEpicTask(Integer id, String nameOfTask, String taskDescription, Status status) {
        if (mapOfDataEpicTask.containsKey(id)) {
            EpicTask curEpic = mapOfDataEpicTask.get(id);
            if (curEpic.getTaskStatus().equals(status)) {
                mapOfDataEpicTask.remove(id);
                EpicTask updateEpic = new EpicTask(id, nameOfTask, taskDescription, status);
                mapOfDataEpicTask.put(id, updateEpic);
            } else {
                System.out.println("Задача типа 'Эпик' не могут самообновляться!");
            }
        } else if (id == null) {
            if (status == Status.NEW) {
                idOfTask++;
                EpicTask newEpic = new EpicTask(idOfTask, nameOfTask, taskDescription, status);
                mapOfDataEpicTask.put(idOfTask, newEpic);

            } else {
                System.out.println("Ошибка! Статус отличный от NEW!");
            }
        } else {
            System.out.println("Задача - " + nameOfTask + " не была инициализирована! " + "/n" +
                    "Задачи №" + id + " несуществует в базе данных!");
        }
    }

    public void createOfUpdateSubTask(Integer inputId, String nameOfTask, String taskDescription,
                                      Status status, Integer idOfEpicTask) {

        if (mapOfDataEpicTask.containsKey(idOfEpicTask)) {
            if (mapOfDataTask.containsKey(inputId)) {
                EpicTask someTaskOfEpic = mapOfDataEpicTask.get(idOfEpicTask);
                HashMap<Integer, SubTask> curMapOfSubTask = someTaskOfEpic.getMapOfSubTusk();
                curMapOfSubTask.remove(inputId);
                mapOfDataTask.remove(inputId);
                SubTask updateSubTask = new SubTask(inputId, nameOfTask, taskDescription, status, idOfEpicTask);
                mapOfDataTask.put(inputId, updateSubTask);
                curMapOfSubTask.put(inputId, updateSubTask);
                someTaskOfEpic.setMapOfSubTusk(curMapOfSubTask);

                checkEpicStatus(someTaskOfEpic);
            }

        } else if (inputId == null) {
            idOfTask ++;
            SubTask newSubTask = new SubTask(idOfTask, nameOfTask, taskDescription, status, idOfEpicTask);
            newSubTask.setTaskId(idOfTask);
            mapOfDataSubTask.put(idOfTask, newSubTask);
            EpicTask someEpicTask = mapOfDataEpicTask.get(idOfEpicTask);
            HashMap<Integer, SubTask> curMapOfSubTask = someEpicTask.getMapOfSubTusk();
            curMapOfSubTask.put(idOfTask, newSubTask);
            checkEpicStatus(someEpicTask);

        }
    }

    public void deleteByIdentifier(Integer inputId) {
        if (mapOfDataTask.containsKey(inputId)) {
            mapOfDataTask.remove(inputId);
            System.out.println("Задача под ID(" + inputId + ") была удалена!");
        } else if (mapOfDataEpicTask.containsKey(inputId)) {
            EpicTask epicTask = mapOfDataEpicTask.get(inputId);
            HashMap<Integer, SubTask> mapOfSubTaskByCurEpic = epicTask.getMapOfSubTusk();
            for (Integer idST : mapOfSubTaskByCurEpic.keySet()) {
                mapOfSubTaskByCurEpic.remove(idST);
            }
            System.out.println("Эпик под ID(" + inputId + ") был удален!");
            checkEpicStatus(epicTask);
        } else if (mapOfDataSubTask.containsKey(inputId)) {
            SubTask subTask = mapOfDataSubTask.get(inputId);
            int idOfCurEpic = subTask.getIdOfEpicTask();
            EpicTask epicTask = mapOfDataEpicTask.get(idOfCurEpic);
            HashMap<Integer, SubTask> mapOfSubTaskByCurEpic = epicTask.getMapOfSubTusk();
            mapOfSubTaskByCurEpic.remove(inputId);
            mapOfDataSubTask.remove(inputId);
            System.out.println("Подзадача с ID(" + inputId + ") была удалена!");
        } else {
            System.out.println("ID(" + inputId + ") несуществует!");
        }
    }

    public void getListAllSubTaskOfCurrentEpic(Integer inputId) {
        EpicTask epicTask = mapOfDataEpicTask.get(inputId);
        HashMap<Integer, SubTask> mapOfSubTaskByCurEpic = epicTask.getMapOfSubTusk();
        for (Integer idST : mapOfSubTaskByCurEpic.keySet()) {
            System.out.println("Подзадача с ID(" + idST + ") - " + mapOfSubTaskByCurEpic.get(idST));
        }
    }

    public void checkEpicStatus(EpicTask epic) {
        HashMap<Integer, SubTask> subtaskOfCurEpic = epic.getMapOfSubTusk();
        int counterOfNewStatus = 0;
        for (SubTask subTask : subtaskOfCurEpic.values()) {
            Status curStatus = subTask.getTaskStatus();
            if (curStatus.equals(Status.NEW)) {
                counterOfNewStatus++;
            }
        }
        int counterOfInProgressStatus = 0;
        for (SubTask subTask : subtaskOfCurEpic.values()) {
            Status curStatus = subTask.getTaskStatus();
            if (curStatus.equals(Status.IN_PROGRESS)) {
                counterOfInProgressStatus++;
            }
        }
        if (counterOfNewStatus == 0 && counterOfInProgressStatus == 0) {
            epic.setTaskStatus(Status.NEW);
        } else if (counterOfNewStatus == 0 && counterOfInProgressStatus > 0) {
            epic.setTaskStatus(Status.IN_PROGRESS);
        } else {
            epic.setTaskStatus(Status.DONE);
        }


    }


}

