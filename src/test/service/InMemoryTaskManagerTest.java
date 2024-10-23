package test.service;

import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import task.SubTask;
import manager.Managers;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest {

    @BeforeEach
    void setUp() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddAndFindTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        Epic epic1 = new Epic();
        epic1.setId(2);
        epic1.setTitle("Epic 1");
        epic1.setDescription("Epic Description 1");

        SubTask subtask1 = new SubTask();
        subtask1.setId(3);
        subtask1.setTitle("Subtask 1");
        subtask1.setDescription("Subtask Description 1");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setEpicId(2);


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
        task1.setTitle("Task with ID 1");
        task1.setDescription("Description 1");


        taskManager.addTask(task1);


        Task task2 = new Task();
        task2.setTitle("Task with generated ID");
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
        task1.setTitle("Task with ID 1");
        task1.setDescription("Description 1");

        taskManager.addTask(task1);

        Task addedTask = taskManager.getTask(task1.getId());
        assertNotNull(addedTask, "Task should be added");
        assertEquals(task1.getTitle(), addedTask.getTitle(), "Task titles should match");
        assertEquals(task1.getDescription(), addedTask.getDescription(), "Task descriptions should match");

    }
}




