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

        historyManager = new InMemoryHistoryManager(10);

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
    }

    @Test
    void testAddMoreThanMaxHistorySize() {
        for (int i = 1; i <= 11; i++) {
            Task task = new Task();
            task.setId(i);
            task.setName("Title " + i);
            task.setDescription("Description " + i);
            task.setStatus(TaskStatus.NEW);
            historyManager.add(task);
        }

        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    public void testUpdateTask() {

        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        manager.addTask(task1); // Добавляем задачу

        // Обновляем задачу
        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setName("Title 1");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.NEW);
        manager.updateTask(updatedTask); // Обновляем задачу

        // Проверяем, что задача обновлена
        Task retrievedTask = manager.getTask(1);
        assertEquals("Updated Description", retrievedTask.getDescription());

        // Проверяем историю
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size()); // Проверяем, что сохранено две версии
        assertEquals("Description 1", history.get(0).getDescription()); // Предыдущая версия
        assertEquals("Updated Description", history.get(1).getDescription()); // Текущая версия
    }

    @Test
    public void testUpdateNonExistentTask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        // Попытка обновить несуществующую задачу
        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setName("Title 1");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.NEW);
        manager.updateTask(updatedTask); // Обновляем несуществующую задачу

        // Проверяем, что история не изменилась
        assertEquals(0, manager.getHistory().size());
    }
}


