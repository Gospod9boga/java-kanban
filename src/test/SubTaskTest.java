package test;
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
        epic1 = new Epic();
        epic1.setId(1);
        epic1.setTitle("Epic Title 4");
        epic1.setDescription("Epic Description 1");
        epic1.setStatus(TaskStatus.NEW);

        subTask1 = new SubTask();
        subTask1.setId(1);
        subTask1.setTitle("SubTask Title 1");
        subTask1.setDescription("SubTask Description 1");
        subTask1.setStatus(TaskStatus.NEW);

        subTask2 = new SubTask();
        subTask2.setId(2);
        subTask2.setTitle("SubTask Title 2");
        subTask2.setDescription("SubTask Description 2");
        subTask2.setStatus(TaskStatus.IN_PROGRESS);

        subTask3 = new SubTask();
        subTask3.setId(3);
        subTask3.setTitle("SubTask Title 3");
        subTask3.setDescription("SubTask Description 3");
        subTask3.setStatus(TaskStatus.DONE);
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