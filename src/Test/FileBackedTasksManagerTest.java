package Test;
import model.EpicTask;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class FileBackedTasksManagerTest {
    public static final Path path = Path.of("file.CSV");
    File file;
    FileBackedTasksManager fileManager ;

    @BeforeEach
    public void beforeEach() {
        file = new File(String.valueOf(path));
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(file.toPath());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void souldTrowExcepyionWhenLoadFromFile() throws ManagerSaveException {
        fileManager = new FileBackedTasksManager(file);
        fileManager.deleteAllCommonTasks();
        fileManager.deleteAllCommonTasks();
        fileManager.deleteAllEpicTasks();
        ManagerSaveException exception = Assertions.assertThrows(
                ManagerSaveException.class,
                () -> {
                    File file = new File("file.CSV");
                    FileBackedTasksManager fromFile = FileBackedTasksManager.loadFromFile(file);
                });
        assertEquals("Файл не был найден", exception.getMessage());
    }

    @Test
    public void shouldSaveAndLoadEmptyTasks() throws ManagerSaveException {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, fileManager.getListAllCommonTasks());
        assertEquals(Collections.EMPTY_LIST, fileManager.getListAllEpicTasks());
        assertEquals(Collections.EMPTY_LIST, fileManager.getListAllSubTasks());

    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() throws ManagerSaveException {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, fileManager.getHistory());
    }

    @Test
    public void shouldCorrectlySaveAndLoadInFile() throws ManagerSaveException {
        Task task = new Task("Task1", "Description1", Status.NEW,
                Instant.ofEpochMilli(4567890l), Duration.ofMinutes(5));
        fileManager.createNewCommonTask(task);
        EpicTask epic = new EpicTask("Epic1", "Decription1", Status.NEW,
                Instant.ofEpochMilli(987654456l), Duration.ofMinutes(7));
        fileManager.createNewEpicTask(epic);
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file);
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(List.of(task), fileManager.getListAllCommonTasks());
        assertEquals(List.of(epic), fileManager.getListAllEpicTasks());
    }


}
