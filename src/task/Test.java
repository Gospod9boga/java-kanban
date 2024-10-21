package task;
import static org.junit.jupiter.api.Assertions.*;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.SubTask;
import task.Task;
import manager.Managers;
class Test {
// Некоторые тесты я не понял как делать например по добавлению эпика самого себя т.е. Добавить его как сабтаск, но метод добавления сабтаска это уже не допускает

    @org.junit.jupiter.api.Test
    void testTasksEqualityById() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Title 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.NEW);

        Task task2 = new Task();
        task2.setId(1);
        task2.setTitle("Title 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);

        Task task3 = new Task();
        task3.setId(2);
        task3.setTitle("Title 3");
        task3.setDescription("Description 3");
        task3.setStatus(TaskStatus.DONE);


        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @org.junit.jupiter.api.Test
    void testSubTasksEqualityById() {
        SubTask subTask1 = new SubTask();
        subTask1.setId(1);
        subTask1.setTitle("SubTask Title 1");
        subTask1.setDescription("SubTask Description 1");
        subTask1.setStatus(TaskStatus.NEW);

        SubTask subTask2 = new SubTask();
        subTask2.setId(1);
        subTask2.setTitle("SubTask Title 2");
        subTask2.setDescription("SubTask Description 2");
        subTask2.setStatus(TaskStatus.IN_PROGRESS);

        SubTask subTask3 = new SubTask();
        subTask3.setId(2);
        subTask3.setTitle("SubTask Title 3");
        subTask3.setDescription("SubTask Description 3");
        subTask3.setStatus(TaskStatus.DONE);


        assertEquals(subTask1, subTask2);
        assertNotEquals(subTask1, subTask3);
    }

    @org.junit.jupiter.api.Test
    void testEpicsEqualityById() {
        Epic epic1 = new Epic();
        epic1.setId(1);
        epic1.setTitle("Epic Title 1");
        epic1.setDescription("Epic Description 1");
        epic1.setStatus(TaskStatus.NEW);

        Epic epic2 = new Epic();
        epic2.setId(1);
        epic2.setTitle("Epic Title 2");
        epic2.setDescription("Epic Description 2");
        epic2.setStatus(TaskStatus.IN_PROGRESS);

        Epic epic3 = new Epic();
        epic3.setId(2);
        epic3.setTitle("Epic Title 3");
        epic3.setDescription("Epic Description 3");
        epic3.setStatus(TaskStatus.DONE);


        assertEquals(epic1, epic2);
        assertNotEquals(epic1, epic3);
    }
// добавление эпика в самого себя
    @org.junit.jupiter.api.Test
    public void testEpicCannotReferenceItselfAsSubTask() {
        Epic epic = new Epic();
        epic.setId(1); // Установим ID для эпика

        SubTask subTask = new SubTask();
        subTask.setId(1); // Установим тот же ID для подзадачи

        // Проверяем, что при попытке установить epicId равным своему ID возникает исключение
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            subTask.setEpicId(subTask.getId()); // Ссылаемся на ID подзадачи
        });

        assertEquals("Подзадача не может ссылаться на самого себя как на эпик.", exception.getMessage());
    }


    // сабтаск нельзя сделать своим эпиком
    @org.junit.jupiter.api.Test
    public void testSubtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic();
        epic.setId(1);

        SubTask subtask = new SubTask();
        subtask.setId(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            subtask.setEpicId(epic.getId());
        });

        String expectedMessage = "Подзадача не может быть своим же эпиком.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
// утилитарный класс возвращает готовые к работе экземпляры
    @org.junit.jupiter.api.Test
     void testGetDefaultTaskManager() {
        assertNotNull(Managers.getDefault());
    }
    @org.junit.jupiter.api.Test
    void testGetDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistory());
    }


    @org.junit.jupiter.api.Test
    public void testAddAndFindTasks() {
        // Создание и добавление обычной задачи
        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        TaskManager taskManager = null;
        taskManager.addTask(task);

        // Создание и добавление эпика
        Epic epic = new Epic();
        epic.setId(2);
        epic.setTitle("Test Epic");
        taskManager.addEpic(epic);

        // Создание и добавление подзадачи
        SubTask subTask = new SubTask();
        subTask.setId(3);
        subTask.setEpicId(2);
        taskManager.addSubtask(subTask);

        // Проверка, что задачи успешно добавлены и найдены по ID
        assertEquals(task, taskManager.getTask(1)); // Проверка обычной задачи
        assertEquals(epic, taskManager.getEpic(2)); // Проверка эпика
        assertEquals(subTask, taskManager.getSubtask(3)); // Проверка подзадачи
    }
}










