import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание задач
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Задача 1");
        task1.setStatus(TaskStatus.NEW);
        manager.addTask(task1);

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Задача 2");
        task2.setStatus(TaskStatus.NEW);
        manager.addTask(task2);


        Epic epic1 = new Epic();
        epic1.setId(3);
        epic1.setTitle("Эпик 1");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setEpicId(epic1.getId());
        manager.addSubtask(subtask1);
        subtask1.setId(4);
        subtask1.setTitle("Подзадача 1");
        subtask1.setStatus(TaskStatus.NEW);


        Subtask subtask2 = new Subtask();
        subtask2.setId(5);
        subtask2.setTitle("Подзадача 2");
        subtask2.setEpicId(epic1.getId());
        subtask2.setStatus(TaskStatus.DONE);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic();
        epic2.setId(6);
        epic2.setTitle("Эпик 2");
        manager.addEpic(epic2);

        Subtask subtask3 = new Subtask();
        subtask3.setId(7);
        subtask3.setTitle("Подзадача 3");
        subtask3.setEpicId(epic2.getId());
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        manager.addSubtask(subtask3);


        System.out.println("Задачи: " + manager.getAllTask());
        System.out.println("Эпики: " + manager.getAllEpics());
        System.out.println("Подзадачи: " + manager.getAllSubtasks());


        task1.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);


        System.out.println("Обновленные задачи:");
        System.out.println("Задача 1: " + task1.getStatus());
        System.out.println("Эпик 1: " + epic1.getStatus());


        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic2.getId());


        System.out.println("После удаления:");
        System.out.println("Задачи: " + manager.getAllTask());
        System.out.println("Эпики: " + manager.getAllEpics());
    }
}