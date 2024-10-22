package test.service;
import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import manager.Managers;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager(10);
    }

    @Test
    void testAdd() {
        Task task1 = new Task(); // Создаем объект Task без аргументов
        task1.setId(1);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size());
    }





}
