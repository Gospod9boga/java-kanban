package test.service;

import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryTaskManager;
import task.Epic;
import task.SubTask;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

class SubTaskTest {
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    Epic epic1;

    @BeforeEach
    void setUp() {
        epic1 = new Epic(1, "Epic Title 4", TaskStatus.NEW, "Epic Description 1");
        subTask1 = new SubTask(1, "SubTask Title 1", TaskStatus.NEW, "SubTask Description 1", epic1.getId());
        subTask2 = new SubTask(2, "SubTask Title 2", TaskStatus.IN_PROGRESS, "SubTask Description 2", epic1.getId());
        subTask3 = new SubTask(3, "SubTask Title 3", TaskStatus.DONE, "SubTask Description 3", epic1.getId());
    }

    @org.junit.jupiter.api.Test
    void subTasksTest1() {
        assertNotEquals(subTask1, subTask2);
        assertNotEquals(subTask1, subTask3);
    }

    @org.junit.jupiter.api.Test
    void testAddSubtaskUpdatesEpic() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addEpic(epic1);
        subTask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subTask1);
        Epic retrievedEpic = taskManager.getEpic(epic1.getId());

        assertNotNull(retrievedEpic, "Epic should not be null");
        assertTrue(retrievedEpic.getSubTaskIds().contains(subTask1.getId()), "Epic should contain the subtask ID");

        subTask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, retrievedEpic.getStatus(), "Epic status should be IN_PROGRESS after adding subtask with IN_PROGRESS status");
    }
}