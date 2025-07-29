package test.service;

import manager.TaskManager;
import org.junit.jupiter.api.*;
import task.*;

import java.io.IOException;

abstract class AbstractTaskManagerTest {
    protected TaskManager manager;

    @BeforeEach
    abstract void setUp() throws IOException;

    @Test
    void testAddTask() {
        Task task = new Task(1, "Task", TaskStatus.NEW, "Desc");
        manager.addTask(task);
        Assertions.assertEquals(1, manager.getAllTask().size());
    }


}
