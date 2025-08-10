package test.service;

import static org.junit.jupiter.api.Assertions.*;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.util.List;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAdd() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size());

        Task retrievedTask = historyManager.getHistory().get(0);
        assertEquals(task1.getId(), retrievedTask.getId());
        assertEquals(task1.getName(), retrievedTask.getName());
        assertEquals(task1.getDescription(), retrievedTask.getDescription());
        assertEquals(task1.getStatus(), retrievedTask.getStatus());
    }

    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "History should be empty initially.");
    }

    @Test
    void testDuplicateTasks() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        historyManager.add(task1);
        historyManager.add(task1); // Добавляем дубликат

        assertEquals(1, historyManager.getHistory().size(), "History should not contain duplicates.");
    }

    @Test
    void testRemoveFromHistoryBeginning() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        historyManager.add(task1);

        Task task2 = new Task();
        task2.setId(2);
        task2.setName("Title 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.NEW);
        historyManager.add(task2);

        historyManager.remove(task1.getId()); // Удаляем первую задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "History should contain one task after removal.");
        assertEquals(task2.getId(), history.get(0).getId(), "The remaining task should be task 2.");
    }

    @Test
    void testRemoveFromHistoryMiddle() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        historyManager.add(task1);

        Task task2 = new Task();
        task2.setId(2);
        task2.setName("Title 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.NEW);
        historyManager.add(task2);

        Task task3 = new Task();
        task3.setId(3);
        task3.setName("Title 3");
        task3.setDescription("Description 3");
        task3.setStatus(TaskStatus.NEW);
        historyManager.add(task3);

        historyManager.remove(task2.getId()); // Удаляем вторую задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "History should contain two tasks after removal.");
        assertEquals(task1.getId(), history.get(0).getId(), "The first task should be task 1.");
        assertEquals(task3.getId(), history.get(1).getId(), "The second task should be task 3.");
    }

    @Test
    void testRemoveFromHistoryEnd() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        historyManager.add(task1);

        Task task2 = new Task();
        task2.setId(2);
        task2.setName("Title 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.NEW);
        historyManager.add(task2);

        historyManager.remove(task2.getId()); // Удаляем последнюю задачу

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "History should contain one task after removal.");
        assertEquals(task1.getId(), history.get(0).getId(), "The remaining task should be task 1.");
    }
}



