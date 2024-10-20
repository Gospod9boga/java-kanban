package task;
import manager.Managers;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
// Некоторые тесты я не понял как делать например по добавлению эпика самого себя т.е. Добавить его как сабтаск, но метод добавления сабтаска это уже не допускает
    
    @Test
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

        // Проверяем равенство
        assertEquals(task1, task2); // task1 и task2 равны по id
        assertNotEquals(task1, task3); // task1 и task3 не равны по id
    }

    @Test
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

    @Test
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
}



