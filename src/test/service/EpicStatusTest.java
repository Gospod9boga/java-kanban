package test.service;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {

    private InMemoryTaskManager taskManager;
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic(0, "Test Epic", TaskStatus.NEW, "Testing epic status");
        taskManager.addEpic(epic);
    }

    @Test
    void whenAllSubtasksNew_thenEpicStatusNew() {
        SubTask sub1 = new SubTask(0, "Subtask 1", TaskStatus.NEW, "Desc", epic.getId());
        SubTask sub2 = new SubTask(0, "Subtask 2", TaskStatus.NEW, "Desc", epic.getId());

        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        assertEquals(TaskStatus.NEW, taskManager.getEpic(epic.getId()).getStatus());
    }

    @Test
    void whenAllSubtasksDone_thenEpicStatusDone() {
        SubTask sub1 = new SubTask(0, "Subtask 1", TaskStatus.DONE, "Desc", epic.getId());
        SubTask sub2 = new SubTask(0, "Subtask 2", TaskStatus.DONE, "Desc", epic.getId());

        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        assertEquals(TaskStatus.DONE, taskManager.getEpic(epic.getId()).getStatus());
    }

    @Test
    void whenSubtasksNewAndDone_thenEpicStatusInProgress() {
        SubTask sub1 = new SubTask(0, "Subtask 1", TaskStatus.NEW, "Desc", epic.getId());
        SubTask sub2 = new SubTask(0, "Subtask 2", TaskStatus.DONE, "Desc", epic.getId());

        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus());
    }

    @Test
    void whenSubtaskInProgress_thenEpicStatusInProgress() {
        SubTask sub1 = new SubTask(0, "Subtask 1", TaskStatus.IN_PROGRESS, "Desc", epic.getId());
        SubTask sub2 = new SubTask(0, "Subtask 2", TaskStatus.NEW, "Desc", epic.getId());

        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(epic.getId()).getStatus());
    }
}
