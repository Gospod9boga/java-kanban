package test.service;

import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import task.SubTask;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

public class InMemoryTaskManagerTest {

    @BeforeEach
    void setUp() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddAndFindTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task(1, "Title 1", TaskStatus.NEW, "Description 1");
        Epic epic1 = new Epic(2, "Epic 1", TaskStatus.NEW, "Description 1", new ArrayList<>());
        SubTask subtask1 = new SubTask(3, "Subtask 1", TaskStatus.NEW, "Subtask Description 1", 2);


        taskManager.addTask(task1);
        taskManager.addTask(epic1);
        taskManager.addTask(subtask1);


        assertEquals(task1, taskManager.getTask(1));
        assertEquals(epic1, taskManager.getTask(2));
        assertEquals(subtask1, taskManager.getTask(3));


        assertNull(taskManager.getTask(4));
    }

    @Test
    void testIdConflict() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Task with ID 1");
        task1.setDescription("Description 1");

        taskManager.addTask(task1);

        Task task2 = new Task();
        task2.setName("Task with generated ID");
        task2.setDescription("Description 2");

        taskManager.addTask(task2);

        assertEquals(task1, taskManager.getTask(1));
        assertNotNull(taskManager.getTask(2));
        assertEquals(task2.getId(), 2);
    }

    @Test
    void testAddTask() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setName("Task with ID 1");
        task1.setDescription("Description 1");

        taskManager.addTask(task1);

        Task addedTask = taskManager.getTask(task1.getId());
        assertNotNull(addedTask, "Task should be added");
        assertEquals(task1.getName(), addedTask.getName(), "Task titles should match");
        assertEquals(task1.getDescription(), addedTask.getDescription(), "Task descriptions should match");

    }
}




