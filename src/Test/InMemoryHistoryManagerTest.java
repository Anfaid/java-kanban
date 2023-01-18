package Test;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    private int ID = 0;

    public Task createTask() {
        return new Task("Task1", "Description", Status.NEW, Instant.now(), Duration.ofMinutes(5));
    }

    public int generatingId() {
        return ++ID;
    }

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldBeAddTaskToHistory() {
        Task task = createTask();
        int taskId = generatingId();
        task.setTaskId(taskId);
        Task task1 = createTask();
        int taskId1 = generatingId();
        task1.setTaskId(taskId1);
        historyManager.addLast(task);
        historyManager.addLast(task1);
        assertEquals(List.of(task, task1), historyManager.getHistory());
    }

    @Test
    public void shouldBeRemoveTaskFromHistory() {
        Task task = createTask();
        int taskId = generatingId();
        task.setTaskId(taskId);
        Task task1 = createTask();
        int taskId1 = generatingId();
        task1.setTaskId(taskId1);
        historyManager.addLast(task);
        historyManager.addLast(task1);
        historyManager.remove(1);
        assertEquals(List.of(task1), historyManager.getHistory());
    }

    @Test
    public void shouldBeGetEmptyHistory() {
        Task task = createTask();
        int taskId = generatingId();
        task.setTaskId(taskId);
        Task task1 = createTask();
        int taskId1 = generatingId();
        task1.setTaskId(taskId1);
        historyManager.addLast(task);
        historyManager.addLast(task1);
        historyManager.remove(1);
        historyManager.remove(2);
        assertEquals(Collections.emptyList(), historyManager.getHistory());
    }







}
