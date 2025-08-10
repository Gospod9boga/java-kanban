package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

class TaskTest {
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        task1 = new Task(1, "Title 1", TaskStatus.NEW, "Description 1");
        task2 = new Task(1, "Title 2", TaskStatus.IN_PROGRESS, "Description 2");
        task3 = new Task(2, "Title 3", TaskStatus.DONE, "Description 3");
    }

    @Test
    void tasksTest1() {
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    void testAddAndGetTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(task1);
        Task retrievedTask = taskManager.getTask(task1.getId());
        assertEquals(task1, retrievedTask);
    }
}












