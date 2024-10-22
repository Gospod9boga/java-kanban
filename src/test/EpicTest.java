package test;
import static org.junit.jupiter.api.Assertions.*;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import manager.Managers;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

class EpicTest {
    Epic epic1;
    Epic epic2;
    Epic epic3;

    @BeforeEach
    void setUp() {
        epic1 = new Epic();
        epic1.setId(1);
        epic1.setTitle("Epic Title 1");
        epic1.setDescription("Epic Description 1");
        epic1.setStatus(TaskStatus.NEW);

        epic2 = new Epic();
        epic2.setId(1);
        epic2.setTitle("Epic Title 2");
        epic2.setDescription("Epic Description 2");
        epic2.setStatus(TaskStatus.IN_PROGRESS);

        epic3 = new Epic();
        epic3.setId(2);
        epic3.setTitle("Epic Title 3");
        epic3.setDescription("Epic Description 3");
        epic3.setStatus(TaskStatus.DONE);
    }

    @org.junit.jupiter.api.Test
    void epicsTest1() {
        assertEquals(epic1, epic2);
        assertNotEquals(epic1, epic3);
    }

    @org.junit.jupiter.api.Test
    void testAddAndGetEpic() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addEpic(epic1);
        Epic retrievedEpic = taskManager.getEpic(epic1.getId());
        assertEquals(epic1, retrievedEpic);
    }
}
