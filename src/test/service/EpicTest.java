package test.service;

import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryTaskManager;
import task.Epic;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

class EpicTest {
    Epic epic1;
    Epic epic2;
    Epic epic3;

    @BeforeEach
    void setUp() {
        epic1 = new Epic(1, "Epic Title 1", TaskStatus.NEW, "Epic Description 1");
        epic2 = new Epic(1, "Epic Title 2", TaskStatus.IN_PROGRESS, "Epic Description 2");
        epic3 = new Epic(2, "Epic Title 3", TaskStatus.DONE, "Epic Description 3");
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
