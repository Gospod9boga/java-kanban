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
    private List<Task> history = new ArrayList<>();
    private int nextId = 1;


    //создание задачи
    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);

    }

    // Получение списка всех задач
    @Override
    public ArrayList<Task> getAllTask() {
        return new ArrayList<Task>(tasks.values());
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с ID " + task.getId() + " не найдена.");
        }
    }

    // Удаление всех задач
    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    // получение по id задачи
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            addToHistory(task);
        }        return tasks.get(id);
    }

    // удаление по id задачи
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    //создание Эпика
    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);

    }

    // Получение списка всех эпиков
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    // Удаление всех эпиков
    @Override
    public void clearAllEpic() {
        subtasks.clear();
        epics.clear();
    }


    // получение по id эпика
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            addToHistory(epic);
        }
        return epics.get(id);
    }

    // удаление по id эпика
    @Override
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
    @Override
    public void updateTask(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Эпик с ID" + epic.getId() + "не найден");
        }
    }


    //создание подзадачи
    @Override
    public void addSubtask(SubTask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTaskIds().add(subtask.getId());

        updateEpicStatus(subtask.getEpicId());

    }

    // Получение списка всех подзадач
    @Override
    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<SubTask>(subtasks.values());
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

    // получение по id подзадачи
    @Override
    public SubTask getSubtask(int id) {
        SubTask subtask = subtasks.get(id);
        if (subtask != null) {
            addToHistory(subtask);
        }
        return subtasks.get(id);
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

    // получение списка подзадач определенного эпика
    @Override
    public ArrayList<SubTask> getAllSubtasksForEpic(int epicId) {
        ArrayList<SubTask> epicSubtasks = new ArrayList<>();
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
    public ArrayList<Task> getHistory(){
        return new ArrayList<>(history);
    }

    // Проверка и добавление задач в историю
    private void addToHistory(Task task) {
                if (!history.contains(task)) {
            if (history.size() == 10) {
                history.remove(0);             }
            history.add(task);
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> epicSubtasks = getAllSubtasksForEpic(epicId);

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


