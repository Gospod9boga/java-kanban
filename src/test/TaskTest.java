package test;

import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryTaskManager;
import task.Task;

import task.TaskStatus;

import org.junit.jupiter.api.BeforeEach;

class TaskTest {
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    void setUp() {
        task1 = new Task();
        task1.setId(1);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        task2 = new Task();
        task2.setId(1);
        task2.setTitle("Title 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);

        task3 = new Task();
        task3.setId(2);
        task3.setTitle("Title 3");
        task3.setDescription("Description 3");
        task3.setStatus(TaskStatus.DONE);
    }


    @org.junit.jupiter.api.Test
    void tasksTest1() {
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @org.junit.jupiter.api.Test
    void testAddAndGetTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(task1);
        Task retrievedTask = taskManager.getTask(task1.getId());
        assertEquals(task1, retrievedTask);
    }
}












