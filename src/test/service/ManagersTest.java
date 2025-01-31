package test.service;

import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryTaskManager;
import task.Task;
import manager.Managers;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

public class ManagersTest {

    // утилитарный класс возвращает готовые к работе экземпляры
    @org.junit.jupiter.api.Test
    void testGetDefaultTaskManager() {
        assertNotNull(Managers.getDefault());
    }

    @org.junit.jupiter.api.Test
    void testGetDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistory());
    }

}
