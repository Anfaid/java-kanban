package service;


import model.*;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;



    public FileBackedTasksManager(File file) {
        this.file = file;

    }

    @Override
    public void createNewCommonTask(Task task) throws IOException {
    super.createNewCommonTask(task);
    save();
    }

    @Override
    public void createNewSubTask(SubTask subTask) throws IOException {
        super.createNewSubTask(subTask);
        save();
    }

    @Override
    public void createNewEpicTask(EpicTask epicTask) throws IOException {
        super.createNewEpicTask(epicTask);
        save();
    }

    @Override
    public Task getCommonTaskById(int id) throws IOException {
     Task newTask = super.getCommonTaskById(id);
     save();
     return newTask;
    }

    @Override
    public SubTask getSubTaskById(int id) throws IOException {
       SubTask newSubtask = super.getSubTaskById(id);
       save();
       return newSubtask;
    }

    @Override
    public EpicTask getEpicTaskById(int id) throws IOException {
        EpicTask newEpicTask = super.getEpicTaskById(id);
        save();
        return newEpicTask;
    }

    public void save() throws IOException {
        String s = file.getName();
        try (FileWriter fileWriter = new FileWriter(s)) {
            if (file.length() == 0) {
                fileWriter.write("id,type,name,status,description,epic\n");
            }
            for (Task task : mapOfDataTask.values()) {
                String string = toString(task);
                fileWriter.write(string);
            }
            for (Task task : mapOfDataSubTask.values()) {
                String string = toString(task);
                fileWriter.write(string);
            }
            for (Task task : mapOfDataEpicTask.values()) {
                String string = toString(task);
                fileWriter.write(string);
            }

    } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static String toString(Task task) {
        String id = Integer.toString(task.getTaskId());
        String name = task.getName();
        String status = task.getTaskStatus().toString();
        String description = task.getTaskDescription();
        String type = null;
        StringBuilder string = new StringBuilder();
        if (task instanceof SubTask) {
            type = "SUBTASK";
            SubTask subTask = (SubTask) task;
            String idOfEpic = Integer.toString(subTask.getIdOfEpicTask());
            string.append(id).append(", ").append(type).append(", ").append(name).append(", ").append(status).append(", ").
                    append(description).append(", ").append(idOfEpic).append("\n");

        } else if (task instanceof EpicTask) {
            type = "EPIC";
            string.append(id).append(", ").append(type).append(", ").append(name).append(", ").append(status).append(", ")
                    .append(description).append("\n");

        } else {
            type = "TASK";
            string.append(id).append(", ").append(type).append(", ").append(name).append(", ").append(status).append(", ")
                    .append(description).append("\n");

        }
        return string.toString();
    }

    static String historyToString(HistoryManager historyManager) {

        List<Task> historyTask = historyManager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < historyTask.size(); i++) {
            sb.append(historyTask.get(i).getTaskId());
            if (i != historyTask.size() - 1) {
                sb.append(",");
            }
        }
        String str = "\n" + sb.toString();
        return str;
    }

    static List<Integer> historyFromString(String str) {
        List<Integer> history = new ArrayList<>();
        String[] strings = str.split(",");
        for (String string : strings) {
            history.add(Integer.parseInt(string));
        }
        return history;
    }

    static Task fromString(String str) {
        String[] split = str.split(",");
        int id = Integer.parseInt(split[0]);
        String name = split[2];
        String taskDescription = split[4];
        Status status = Status.valueOf(split[3].trim());
        int epicId = 0;
        if (split.length == 6) {
            epicId = Integer.parseInt(split[5].trim());
        }
        if ("EPIC".equals(split[1].trim())) {
            EpicTask epic = new EpicTask(name, taskDescription, status);
            epic.setTaskId(id);
            epic.setTaskStatus(Status.valueOf(split[3].trim().toUpperCase()));
            return epic;
        } else if ("SUBTASK".equals(split[1].trim())) {
            SubTask subTask = new SubTask(name, taskDescription,status ,epicId);
            subTask.setTaskId(id);
            return subTask;
        } else {
            Task task = new Task(name, taskDescription, status);
            task.setTaskId(id);
            return task;
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        List<Integer> historyList = new ArrayList<>();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.startsWith("i")) {
                    String Id = line;
                } else if (line.isBlank()) {
                    line = bufferedReader.readLine();
                    historyList = historyFromString(line);
                } else {
                    String[] lineElements = line.split(", ");
                    if (lineElements[1].equals("EPIC")) {
                        EpicTask newTask = (EpicTask) fromString(line);
                        int newTaskId = newTask.getTaskId();
                        fileBackedTasksManager.mapOfDataEpicTask.put(newTaskId, newTask);
                        System.out.println(FileBackedTasksManager.toString(fileBackedTasksManager.mapOfDataEpicTask.get(newTaskId)));
                    } else if (lineElements[1].equals("SUBTASK")) {
                        SubTask newTask = (SubTask) fromString(line);
                        int newTaskId = newTask.getTaskId();
                        fileBackedTasksManager.mapOfDataSubTask.put(newTaskId, newTask);
                        System.out.println(FileBackedTasksManager.toString(fileBackedTasksManager.mapOfDataSubTask.get(newTaskId)));
                    } else {
                        Task newTask = fromString(line);
                        int newTaskId = newTask.getTaskId();
                        fileBackedTasksManager.mapOfDataTask.put(newTaskId, newTask);
                        System.out.println(FileBackedTasksManager.toString(fileBackedTasksManager.mapOfDataTask.get(newTaskId)));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл был не найден!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(historyList.toString());

        return fileBackedTasksManager;
    }

    }



