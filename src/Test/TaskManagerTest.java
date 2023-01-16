package Test;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.ManagerSaveException;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    @Test
    public void shouldCreateCommonTask() throws IOException, ManagerSaveException {
        Task newTask = new Task("Task1", "Description", Status.NEW,
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewCommonTask(newTask);

        assertEquals(newTask, taskManager.getCommonTaskById(newTask.getTaskId()));
        assertNotNull(taskManager.getCommonTaskById(newTask.getTaskId()));
        List<Task> taskList = taskManager.getListAllCommonTasks();
        assertEquals(List.of(newTask), taskList);
        assertEquals(Status.NEW, newTask.getTaskStatus());
    }

    @Test
    public void shouldCreateSubtask() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW,
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        SubTask subTask = new SubTask("Task1", "Description", Status.NEW,
                epic.getTaskId(), Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewSubTask(subTask);

        assertEquals(epic, taskManager.getEpicTaskById(epic.getTaskId()));
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getTaskId()));
        assertNotNull(taskManager.getSubTaskById(subTask.getTaskId()));
        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
        List<SubTask> subTasks = taskManager.getListAllSubTasks();
        assertEquals(List.of(subTask), subTasks);
        assertEquals(List.of(subTask.getTaskId()), epic.getTaskId());
        assertEquals(Status.NEW, subTask.getTaskStatus());
        assertEquals(epic.getTaskId(), subTask.getIdOfEpicTask());
    }

    @Test
    public void shouldCreateEpicTask() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW,
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        assertEquals(epic, taskManager.getEpicTaskById(epic.getTaskId()));
        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
        List<EpicTask> epics = taskManager.getListAllEpicTasks();
        assertEquals(List.of(epic), epics);
        assertEquals(Status.NEW, epic.getTaskStatus());
        assertEquals(Collections.emptyList(), epic.getListOfSubTask());
    }

    @Test
    public void shouldUpdateCommonTask() throws IOException, ManagerSaveException {
        Task task = new Task("Task1", "Description", Status.NEW,
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewCommonTask(task);

        assertNotNull(taskManager.getCommonTaskById(task.getTaskId()));
        task.setTaskStatus(Status.DONE);
        taskManager.updateCommonTask(task);
        assertEquals(Status.DONE, task.getTaskStatus());
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        SubTask subTask = new SubTask("Task1", "Description", Status.NEW, epic.getTaskId(),
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewSubTask(subTask);

        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
        assertNotNull(taskManager.getSubTaskById(subTask.getTaskId()));
        List<Integer> subTasks = epic.getListOfSubTask();
        assertEquals(List.of(subTask.getTaskId()), subTasks);
        subTask.setTaskDescription("new Descript.");
        taskManager.updateSubtask(subTask);
        assertEquals("new Descript.", subTask.getTaskDescription());
    }

    @Test
    public void shouldUpdateEpicTask() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW,
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);
        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
        epic.setTaskDescription("new descrip.");
        taskManager.updateEpicTask(epic);
        assertEquals("new descrip.", epic.getTaskDescription());
        assertEquals(epic, taskManager.getEpicTaskById(epic.getTaskId()));
    }

    @Test
    public void shouldBeGetCommonTaskById() throws IOException, ManagerSaveException {
        Task task = new Task("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewCommonTask(task);

        assertNotNull(taskManager.getCommonTaskById(task.getTaskId()));
        assertEquals(task, taskManager.getCommonTaskById(task.getTaskId()));
    }

    @Test
    public void shouldBeGetSubtaskById() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        SubTask subTask = new SubTask("Task1", "Description", Status.NEW, epic.getTaskId(),
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewSubTask(subTask);

        assertEquals(subTask, taskManager.getSubTaskById(subTask.getTaskId()));
        assertNotNull(taskManager.getSubTaskById(subTask.getTaskId()));
    }

    @Test
    public void shouldBeGetEpicTaskById() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        assertEquals(epic, taskManager.getEpicTaskById(epic.getTaskId()));
        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
    }

    @Test
    public void shouldThrowExceptionWhenSubtaskIsNull() {
        SubTask subTask = new SubTask("Task1", "Description", Status.NEW, 5,
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        assertThrows(NullPointerException.class, () -> taskManager.getSubTaskById(subTask.getTaskId()));
    }

    @Test
    public void shouldThrowExceptionWhenCommonTaskIsNull() {
        Task task = new Task("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        assertThrows(NullPointerException.class, () -> taskManager.getCommonTaskById(task.getTaskId()));
    }

    @Test
    public void shouldThrowExceptionWhenEpicTaskIsNull() {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        assertThrows(NullPointerException.class, () -> taskManager.getEpicTaskById(epic.getTaskId()));
    }

    @Test
    public void shouldBeDeleteCommonTaskById() throws IOException, ManagerSaveException {
        Task task = new Task("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewCommonTask(task);

        assertNotNull(taskManager.getCommonTaskById(task.getTaskId()));
        assertEquals(task, taskManager.getCommonTaskById(task.getTaskId()));
        taskManager.deleteCommonTaskById(task.getTaskId());
        assertNull(taskManager.getCommonTaskById(task.getTaskId()));
    }

    @Test
    public void shouldBeDeleteSubtaskById() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        SubTask subTask = new SubTask("Task1", "Description", Status.NEW, epic.getTaskId(),
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewSubTask(subTask);

        assertNotNull(taskManager.getSubTaskById(subTask.getTaskId()));
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getTaskId()));
        taskManager.deleteCommonTaskById(subTask.getTaskId());
        assertNull(taskManager.getSubTaskById(subTask.getTaskId()));
    }

    @Test
    public void shouldBeDeleteEpicTaskById() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        assertEquals(epic, taskManager.getEpicTaskById(epic.getTaskId()));
        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
        taskManager.deleteEpicTaskById(epic.getTaskId());
        assertNull(taskManager.getEpicTaskById(epic.getTaskId()));
    }

    @Test
    public void shouldBeDeleteAllCommonTask() throws IOException, ManagerSaveException {
        Task task = new Task("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewCommonTask(task);

        assertNotNull(taskManager.getCommonTaskById(task.getTaskId()));
        assertEquals(task, taskManager.getCommonTaskById(task.getTaskId()));
        taskManager.deleteAllCommonTasks();
        assertNull(taskManager.getCommonTaskById(task.getTaskId()));
    }

    @Test
    public void shouldBeDeleteAllSubtask() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        SubTask subTask = new SubTask("Task1", "Description", Status.NEW, epic.getTaskId(),
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewSubTask(subTask);

        assertNotNull(taskManager.getSubTaskById(subTask.getTaskId()));
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getTaskId()));
        taskManager.deleteAllCommonTasks();
        assertNull(taskManager.getSubTaskById(subTask.getTaskId()));

    }

    @Test
    public void shouldBeDeleteAllEpicTask() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        assertEquals(epic, taskManager.getEpicTaskById(epic.getTaskId()));
        assertNotNull(taskManager.getEpicTaskById(epic.getTaskId()));
        taskManager.deleteAllEpicTasks();
        assertNull(taskManager.getEpicTaskById(epic.getTaskId()));
    }

    @Test
    public void shouldBeGetListAllCommonTask() throws ManagerSaveException {
        assertNotNull(taskManager.getListAllCommonTasks(), "Лист тасков полон.");
        assertEquals(new ArrayList(taskManager.getTaskMap().values()), taskManager.getListAllCommonTasks());

    }

    @Test
    public void shouldBeGetListAllSubtask() throws ManagerSaveException {
        assertNotNull(taskManager.getListAllSubTasks(),"Лист сабтасков полон.");
        assertEquals(new ArrayList(taskManager.getSubtaskMap().values()),taskManager.getListAllSubTasks() );
    }

    @Test
    public void shouldBeGetListAllEpicTask() throws ManagerSaveException {
        assertNotNull(taskManager.getListAllEpicTasks(),"Лист эпиков полон.");
        assertEquals(new ArrayList(taskManager.getEpicMap().values()), taskManager.getListAllEpicTasks());
    }

    @Test
    public void shouldBegetListOfSubTaskByCurEpic() throws IOException, ManagerSaveException {
        EpicTask epic = new EpicTask("Task1", "Description", Status.NEW, Duration.ofMinutes(5),
                Instant.ofEpochMilli(4567890));
        taskManager.createNewEpicTask(epic);

        SubTask subTask = new SubTask("Task1", "Description", Status.NEW, epic.getTaskId(),
                Duration.ofMinutes(5), Instant.ofEpochMilli(4567890));
        taskManager.createNewSubTask(subTask);
    }
}
