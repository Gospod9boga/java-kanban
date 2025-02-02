package test.service;
import static org.junit.jupiter.api.Assertions.*;
import manager.Managers;

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
