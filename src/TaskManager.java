import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;


    //создание задачи
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);

    }

    // Получение списка всех задач
    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    // Обновление задачи
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с ID " + task.getId() + " не найдена.");
        }
    }

    // Удаление всех задач
    public void clearAllTasks() {
        tasks.clear();
    }

    // получение по id задачи
    public Task getTask(int id) {
        return tasks.get(id);
    }

    // удаление по id задачи
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    //создание Эпика
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);

    }

    // Получение списка всех эпиков
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    // Удаление всех эпиков
    public void clearAllEpic() {
        subtasks.clear();
        epics.clear();
    }


    // получение по id эпика
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    // удаление по id эпика
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {

            for (Integer subtaskId : epic.getSubTaskIds()) {
                subtasks.remove(subtaskId);
            }

            epics.remove(id);
        } else {
            System.out.println("Эпик с ID " + id + " не найден.");
        }
    }


    // Обновление эпика
    public void updateTask(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с ID" + epic.getId() + "не найден");
        }
    }


    //создание подзадачи
    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTaskIds().add(subtask.getId());

        updateEpicStatus(subtask.getEpicId());

    }

    // Получение списка всех подзадач
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    // Удаление всех подзадач
    public void clearAllSubtask() {
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    // получение по id подзадачи
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    // удаление по id подзадачу
    public void deleteSubtaskById(int id) {
        for (Epic epic : epics.values()) {
            if (epic.getSubTaskIds().remove(Integer.valueOf(id))) {
                updateEpicStatus(epic.getId());
            }
        }
        subtasks.remove(id);
    }

    // Обновление подзадачи
    public void updateTask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Подзадача с ID " + subtask.getId() + " не найдена");
        }
    }

    // получение списка подзадач определенного эпика
    public ArrayList<Subtask> getAllSubtasksForEpic(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            Subtask newSubtask = subtasks.get(id);
            if (newSubtask.getEpicId() == epicId) {
                epicSubtasks.add(newSubtask);
            }
        }
        return epicSubtasks;
    }

    // обновление статуса
    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = getAllSubtasksForEpic(epicId);

        if (epicSubtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean allNew = true;
            boolean allDone = true;

            for (int i = 0; i < epicSubtasks.size(); i++) {
                Subtask subtask = epicSubtasks.get(i);
                if (subtask.getStatus() != TaskStatus.NEW) {
                    allNew = false;
                }
                if (subtask.getStatus() != TaskStatus.DONE) {
                    allDone = false;
                }
            }

            if (allDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (allNew) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

}


