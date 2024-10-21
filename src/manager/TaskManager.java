package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    //создание задачи
    void addTask(Task task);

    // Получение списка всех задач
    List<Task> getAllTask();

    // Обновление задачи
    void updateTask(Task task);

    // Удаление всех задач
    void clearAllTasks();

    // получение по id задачи
    Task getTask(int id);

    // удаление по id задачи
    void deleteTaskById(int id);

    //создание Эпика
    void addEpic(Epic epic);

    // Получение списка всех эпиков
    List<Epic> getAllEpics();

    // Удаление всех эпиков
    void clearAllEpic();

    // получение по id эпика
    Epic getEpic(int id);

    // удаление по id эпика
    void deleteEpicById(int id);

    // Обновление эпика
    void updateTask(Epic epic);

    //создание подзадачи
    void addSubtask(SubTask subtask);

    // Получение списка всех подзадач
    List<SubTask> getAllSubtasks();

    // Удаление всех подзадач
    void clearAllSubtask();

    // получение по id подзадачи
    SubTask getSubtask(int id);

    // удаление по id подзадачу
    void deleteSubtaskById(int id);

    // Обновление подзадачи
    void updateTask(SubTask subtask);

    // получение списка подзадач определенного эпика
    List<SubTask> getAllSubtasksForEpic(int epicId);


    // Возврат последних 10 просмотренных Задач
    List<Task> getHistory();

}
