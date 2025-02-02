package test.service;
import static org.junit.jupiter.api.Assertions.*;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
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
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size());

        Task retrievedTask = historyManager.getHistory().get(0);
        assertEquals(task1.getId(), retrievedTask.getId());
        assertEquals(task1.getTitle(), retrievedTask.getTitle());
        assertEquals(task1.getDescription(), retrievedTask.getDescription());
        assertEquals(task1.getStatus(), retrievedTask.getStatus());
    }

    @Test
    public void testUpdateTask() {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        manager.addTask(task1);

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("Title 1");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.NEW);
        manager.updateTask(updatedTask);

        Task retrievedTask = manager.getTask(1);
        assertEquals("Updated Description", retrievedTask.getDescription());

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());

        assertEquals("Updated Description", history.get(0).getDescription());
    }

    @Test
    public void testUpdateNonExistentTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        // Попытка обновить несуществующую задачу
        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("Title 1");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.NEW);
        manager.updateTask(updatedTask); // Обновляем несуществующую задачу

        // Проверяем, что история не изменилась
        assertEquals(0, manager.getHistory().size());
    }
}


