package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    //создание Задачи
    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    //создание Эпика
    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    //создание Подзадачи
    @Override
    public void addSubtask(SubTask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTaskIds().add(subtask.getId());

        updateEpicStatus(subtask.getEpicId());
    }

    // Получение списка всех задач
    @Override
    public List<Task> getAllTask() {
        return new ArrayList<Task>(tasks.values());
    }

    // Получение списка всех эпиков
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    // Получение списка всех подзадач
    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<SubTask>(subtasks.values());
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            // Сохраняем старую задачу в истории
            historyManager.add(tasks.get(task.getId())); // Добавьте это, чтобы сохранить старую задачу
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с ID " + task.getId() + " не найдена.");
        }
    }

    // Обновление эпика
    @Override
    public void updateTask(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с ID" + epic.getId() + "не найден");
        }
    }

    // Обновление подзадачи
    @Override
    public void updateTask(SubTask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        } else {
            System.out.println("Подзадача с ID " + subtask.getId() + " не найдена");
        }
    }

    // Удаление всех задач
    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    // Удаление всех эпиков
    @Override
    public void clearAllEpic() {
        subtasks.clear();
        epics.clear();
    }

    // Удаление всех подзадач
    @Override
    public void clearAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    // получение по id задачи
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    // получение по id эпика
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    // получение по id подзадачи
    @Override
    public SubTask getSubtask(int id) {
        SubTask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    // удаление по id задачи
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    // удаление по id эпика
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubTaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Эпик с ID " + id + " не найден.");
        }
    }

    // удаление по id подзадачу
    @Override
    public void deleteSubtaskById(int id) {
        for (Epic epic : epics.values()) {
            if (epic.getSubTaskIds().remove(Integer.valueOf(id))) {
                updateEpicStatus(epic.getId());
            }
        }
        subtasks.remove(id);
        historyManager.remove(id);
    }

    // получение списка подзадач определенного эпика
    @Override
    public List<SubTask> getAllSubtasksForEpic(int epicId) {
        List<SubTask> epicSubtasks = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            SubTask newSubtask = subtasks.get(id);
            if (newSubtask.getEpicId() == epicId) {
                epicSubtasks.add(newSubtask);
            }
        }
        return epicSubtasks;
    }

    // получение списка 10 последний задач
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        List<SubTask> epicSubtasks = getAllSubtasksForEpic(epicId);

        if (epicSubtasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean allNew = true;
            boolean allDone = true;

            for (int i = 0; i < epicSubtasks.size(); i++) {
                SubTask subtask = epicSubtasks.get(i);
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


