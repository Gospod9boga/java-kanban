package test.service;

import manager.FileBackedTaskManager;
import manager.ManagerSaveException;
import org.junit.jupiter.api.*;
import task.*;
import java.io.*;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTask().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }

    @Test
    void testSaveAndLoadTasks() {
        // Добавляем задачи
        Task task = new Task(1, "Task 1", TaskStatus.NEW, "Description");
        Epic epic = new Epic(2, "Epic 1", TaskStatus.NEW, "Epic Description");
        SubTask subTask = new SubTask(3, "SubTask 1", TaskStatus.DONE, "SubTask Description", 2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subTask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getAllTask().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());

        Task loadedTask = loadedManager.getTask(1);
        assertEquals("Task 1", loadedTask.getName());

        SubTask loadedSubTask = loadedManager.getSubtask(3);
        assertEquals(2, loadedSubTask.getEpicId());
    }

    @Test
    void testLoadFromNonExistentFile() {
        File nonExistentFile = new File("nonexistent.csv");
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.loadFromFile(nonExistentFile));
    }
}
