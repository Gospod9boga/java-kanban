public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        // Создаём первую задачу
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Первая задача");
        task1.setDescription("Это описание первой задачи.");
        task1.setStatus(TaskStatus.NEW);

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Вторая задача");
        task2.setDescription("Это описание второй задачи.");
        task2.setStatus(TaskStatus.IN_PROGRESS);

        Epic epic1 = new Epic();
        epic1.setEpicId(3);
        epic1.setTitle("Первый Эпик");
        epic1.setDescription("Это описание первого эпика");
        epic1.setStatus(TaskStatus.NEW);

        Subtask subtask1= new Subtask();
        subtask1.setEpicId(4);
        subtask1.setTitle("Подзадача первого эпика");
        subtask1.setDescription("Это описание первой подзадачи");
        subtask1.setStatus(TaskStatus.DONE);

        epic1.addSubTaskId(4);


        Subtask subtask2= new Subtask();
        subtask2.setEpicId(5);
        subtask2.setTitle("Подзадача первого эпика");
        subtask2.setDescription("Это описание второй подзадачи");
        subtask2.setStatus(TaskStatus.IN_PROGRESS);

        epic1.addSubTaskId(5);


        Epic epic2 = new Epic();
        epic2.setEpicId(6);
        epic2.setTitle("Второй Эпик");
        epic2.setDescription("Это описание Втрого эпика");
        epic2.setStatus(TaskStatus.NEW);

        Subtask subtask3= new Subtask();
        subtask3.setEpicId(7);
        subtask3.setTitle("Подзадача второго эпика");
        subtask3.setDescription("Это описание единственной подзадачи");
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        epic1.addSubTaskId(7);

        System.out.println("Задачи:");
        System.out.println(task1);
        System.out.println(task2);

        // Распечатка эпиков
        System.out.println("\nЭпики:");
        System.out.println(epic1);
        System.out.println(epic2);
        Subtask[] subtasks = manager.getSubtasksByEpic(6);
        manager.printSubtasks(subtasks);


        }
    }









