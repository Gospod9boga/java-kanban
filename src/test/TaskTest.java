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
        task1 = new Task(1, "Title 1", TaskStatus.NEW, "Description 1");
        task2 = new Task(1, "Title 2", TaskStatus.IN_PROGRESS, "Description 2");
        task3 = new Task(2, "Title 3", TaskStatus.DONE, "Description 3");
    }


    @org.junit.jupiter.api.Test
    void TasksTest1() {
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












