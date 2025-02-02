package test.service;
import static org.junit.jupiter.api.Assertions.*;
import manager.InMemoryHistoryManager;
import manager.TaskManager;
import task.Epic;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import task.SubTask;
import task.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

public class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
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

    @Test
    void testUpdateTaskFieldsImpactOnManager() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Original Title");
        task1.setDescription("Original Description");
        task1.setStatus(TaskStatus.NEW);
        taskManager.addTask(task1);

        // Изменяем поля задачи
        task1.setTitle("Updated Title");
        task1.setDescription("Updated Description");

        // Обновляем задачу в менеджере
        taskManager.updateTask(task1);

        // Проверяем, что изменения отразились в менеджере
        Task updatedTask = taskManager.getTask(task1.getId());
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
    }

    @Test
    void testRemoveTaskUpdatesReferences() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task to Remove");
        task1.setDescription("Description to Remove");
        taskManager.addTask(task1);

        // Удаляем задачу
        taskManager.deleteTaskById(1);

        // Проверяем, что задача была удалена
        assertNull(taskManager.getTask(1), "Task should be removed");
    }

    @Test
    void testRemoveSubtaskUpdatesEpic() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        epic.setId(1);
        epic.setTitle("Epic Title");
        epic.setDescription("Epic Description");
        taskManager.addTask(epic);

        SubTask subtask = new SubTask();
        subtask.setId(2);
        subtask.setTitle("Subtask Title");
        subtask.setDescription("Subtask Description");
        subtask.setEpicId(epic.getId());
        taskManager.addTask(subtask);

        // Удаляем подзадачу
        taskManager.deleteSubtaskById(2);

        // Проверяем, что подзадача была удалена
        assertNull(taskManager.getSubtask(2), "Subtask should be removed");
    }

    @Test
    void addToHistory_TheSameTaskMultipleTimes_noDuplicatesInHistory() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        taskManager.addTask(task1);

        // Запрашиваем задачу несколько раз
        historyManager.add(taskManager.getTask(1));
        historyManager.add(taskManager.getTask(1));
        historyManager.add(taskManager.getTask(1));

        // Проверяем историю
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size()); // Должна быть только одна запись в истории
        assertEquals(task1.getId(), history.get(0).getId());
    }

    @Test
    void removeTask_itRemovedFromHistory() {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);
        taskManager.addTask(task1);

        // Запрашиваем задачу
        taskManager.getTask(1);

        // Удаляем задачу
        taskManager.deleteTaskById(1);

        // Проверяем, что задача удалена из истории
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty()); // История должна быть пустой
    }

    @Test
    void removeEpic_itAndHisSubtasksRemovedFromHistory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем эпик
        Epic epic = new Epic();
        taskManager.addEpic(epic);

        // Создаем подзадачи и добавляем их к эпикам
        SubTask subtask1 = new SubTask();
        subtask1.setEpicId(epic.getId());
        taskManager.addSubtask(subtask1);

        SubTask subtask2 = new SubTask();
        subtask2.setEpicId(epic.getId());
        taskManager.addSubtask(subtask2);

        // Получаем эпик и подзадачи, чтобы они добавились в историю
        taskManager.getEpic(epic.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());

        // Проверяем, что эпик и подзадачи находятся в истории
        List<Task> historyBeforeRemoval = taskManager.getHistory();
        assertEquals(3, historyBeforeRemoval.size(), "История должна содержать 3 задачи (1 эпик + 2 подзадачи)");

        // Удаляем эпик
        taskManager.deleteEpicById(epic.getId());

        // Проверяем, что эпик и подзадачи удалены из истории
        List<Task> historyAfterRemoval = taskManager.getHistory();
        assertTrue(historyAfterRemoval.isEmpty(), "История не должна содержать задач после удаления эпика");
    }
}






