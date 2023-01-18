package service;


import model.*;


import javax.imageio.IIOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;



    public FileBackedTasksManager(File file) {
        this.file = file;

    }

    @Override
    public List<Task> getHistory() throws ManagerSaveException {
        save();
        return super.historyManager.getHistory();
    }

    @Override
    public List<Task> getListAllCommonTasks() throws ManagerSaveException {
        save();
        return super.getListAllCommonTasks();
    }

    @Override
    public List<SubTask> getListAllSubTasks() throws ManagerSaveException {
        save();
        return super.getListAllSubTasks();
    }

    @Override
    public List<EpicTask> getListAllEpicTasks() throws ManagerSaveException {
        save();
        return super.getListAllEpicTasks();
    }

    @Override
    public void createNewCommonTask(Task task) throws ManagerSaveException {
    super.createNewCommonTask(task);
    save();
    }

    @Override
    public void updateCommonTask(Task task) throws ManagerSaveException {
    super.updateCommonTask(task);
    save();
    }

    @Override
    public void createNewSubTask(SubTask subTask) throws ManagerSaveException {
        super.createNewSubTask(subTask);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) throws ManagerSaveException {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void createNewEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.createNewEpicTask(epicTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public Task getCommonTaskById(int id) throws ManagerSaveException {
     Task newTask = super.getCommonTaskById(id);
     save();
     return newTask;
    }

    @Override
    public SubTask getSubTaskById(int id) throws ManagerSaveException {
       SubTask newSubtask = super.getSubTaskById(id);
       save();
       return newSubtask;
    }

    @Override
    public EpicTask getEpicTaskById(int id) throws ManagerSaveException {
        EpicTask newEpicTask = super.getEpicTaskById(id);
        save();
        return newEpicTask;
    }

    @Override
    public void deleteAllCommonTasks() throws ManagerSaveException {
        super.deleteAllCommonTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() throws ManagerSaveException {
        super.deleteAllSubTasks();
        save();
    }


    @Override
    public void deleteAllEpicTasks() throws ManagerSaveException {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteCommonTaskById(int id) throws ManagerSaveException {
        super.deleteCommonTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) throws ManagerSaveException {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(int id) throws ManagerSaveException {
        super.deleteEpicTaskById(id);
        save();
    }



    public void save() throws ManagerSaveException {
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
            throw new ManagerSaveException("Файл не был распознан или прочитан!");
        }
    }

    static String toString(Task task) {
        String id = Integer.toString(task.getTaskId());
        String name = task.getName();
        String status = task.getTaskStatus().toString();
        String description = task.getTaskDescription();
        Instant startTime = task.getStartTime();
        Duration duration = task.getDuration();
        String type = null;
        StringBuilder stringBuilder = new StringBuilder();
        if (task instanceof SubTask) {
            type = "SUBTASK";
            SubTask subTask = (SubTask) task;
            String idOfEpic = Integer.toString(subTask.getIdOfEpicTask());
            stringBuilder.append(id).append(", ").append(type).append(", ").append(name).append(", ").append(status)
                    .append(", ").
                    append(description).append(", ").append(idOfEpic).append(startTime).append(", ").append(duration).append(", ")
                    .append(", ").append("\n");

        } else if (task instanceof EpicTask) {
            type = "EPIC";
            stringBuilder.append(id).append(", ").append(type).append(", ").append(name).append(", ")
                    .append(status).append(", ")
                    .append(description).append(", ").append(startTime).append(", ").append(duration)
                    .append(", ").append("\n");

        } else {
            type = "TASK";
            stringBuilder.append(id).append(", ").append(type).append(", ").append(name).append(", ").append(status).append(", ")
                    .append(description).append(", ").append(startTime).append(", ").append(duration)
                    .append(", ").append("\n");

        }
        return stringBuilder.toString();
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
        String[] elem = str.split(",");
        int id = Integer.parseInt(elem[0]);
        String name = elem[2];
        String description = elem[4];
        Status status = Status.valueOf(elem[3].trim());
        String stringStart = elem[5].trim();
        Instant startTime = Instant.parse(stringStart);
        Duration duration = Duration.ZERO;
        if (elem[6].endsWith("M")) {
            String subTring = elem[6].substring(elem[6].indexOf('T') + 1, elem[6].indexOf('M'));
            duration = Duration.ofMinutes(Long.parseLong(subTring));
        } else {
            String subTring = elem[6].substring(elem[6].indexOf('T') + 1, elem[6].indexOf('S'));
            duration = Duration.ofMinutes(Long.parseLong(subTring));
        }
        int epicNumber = 0;
        if (elem.length == 8) {
            epicNumber = Integer.parseInt(elem[7].trim());
        }
        if ("EPIC".equals(elem[1].trim())) {
            EpicTask epic = new EpicTask(name, description, status, startTime, duration);
            epic.setTaskId(id);
            epic.setTaskStatus(Status.valueOf(elem[3].trim().toUpperCase()));
            return epic;
        } else if ("SUBTASK".equals(elem[1].trim())) {
            SubTask subTask = new SubTask(name, description, status, epicNumber, startTime, duration);
            subTask.setTaskId(id);

            return subTask;
        } else {
            Task task = new Task(name, description, status, startTime, duration);
            task.setTaskId(id);
            return task;
        }
    }

        public static FileBackedTasksManager loadFromFile (File file) throws ManagerSaveException {
            List<Integer> historyArr = new ArrayList<>();
            FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                while (fileReader.ready()) {
                    String line = fileReader.readLine();
                    if (line.startsWith("i")) {
                        String lineId = line;
                    } else if (line.isBlank()) {
                        line = fileReader.readLine();
                        historyArr = historyFromString(line);
                    } else {
                        String[] lineElements = line.split(", ");
                        if (lineElements[1].equals("EPIC")) {
                            EpicTask newTask = (EpicTask) fromString(line);
                            int newTaskId = newTask.getTaskId();
                            fileBackedTasksManager.mapOfDataEpicTask.put(newTaskId, newTask);
                            System.out.println(FileBackedTasksManager.toString(fileBackedTasksManager.
                                    mapOfDataEpicTask.get(newTaskId)));
                        } else if (lineElements[1].equals("SUBTASK")) {
                            SubTask newTask = (SubTask) fromString(line);
                            int newTaskId = newTask.getTaskId();
                            fileBackedTasksManager.mapOfDataSubTask.put(newTaskId, newTask);
                            System.out.println(FileBackedTasksManager.toString(fileBackedTasksManager.
                                    mapOfDataSubTask.get(newTaskId)));
                        } else {
                            Task newTask = fromString(line);
                            int newTaskId = newTask.getTaskId();
                            fileBackedTasksManager.mapOfDataTask.put(newTaskId, newTask);
                            System.out.println(FileBackedTasksManager.toString(fileBackedTasksManager.mapOfDataTask
                                    .get(newTaskId)));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                throw new ManagerSaveException("Файл не найден");
            } catch (IIOException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(historyArr);
            return fileBackedTasksManager;
        }
    }





