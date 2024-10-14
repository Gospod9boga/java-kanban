
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

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
    public ArrayList<Task> printAllTasks() {
        ArrayList<Task> tasksId = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            Task newTask = tasks.get(id);
            tasksId.add(newTask);
        }
        return tasksId;
    }

    // Обновление задачи
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
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
    public void deleteIDTask(int id) {
        tasks.remove(id);
    }


    //создание Эпика
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);

    }

    // Получение списка всех эпиков
    public ArrayList<Epic> printAllEpic() {
        ArrayList<Epic> epicId = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            Epic newEpic = epics.get(id);
            epicId.add(newEpic);
        }
        return epicId;
    }

    // Удаление всех эпиков
    public void clearAllEpic() {
        epics.clear();
    }

    // получение по id эпика
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    // удаление по id эпика
    public void deleteIDEpic(int id) {
        epics.remove(id);
    }

    // Обновление эпика
    public void updateTask(Epic epic) {
        tasks.put(epic.getId(), epic);
    }


    //создание подзадачи
    public void addSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getEpicId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubTaskIds().add(subtask.getId());

    }

    // Получение списка всех подзадач
    public ArrayList<Subtask> printAllSubtask() {
        ArrayList<Subtask> subtaskId = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            Subtask newSubtask = subtasks.get(id);
            subtaskId.add(newSubtask);
        }
        return subtaskId;
    }

    // Удаление всех подзадач
    public void clearAllSubtask() {
        subtasks.clear();
    }

    // получение по id подзадачи
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    // удаление по id подзадачу
    public void deleteIDSubtask(int id) {
        subtasks.remove(id);
    }

    // Обновление подзадачи
    public void updateTask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    // получение списка подзадач определенного эпика
    public ArrayList<Subtask> printAllSubtasksForEpic(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer id : subtasks.keySet()) {
            Subtask newSubtask = subtasks.get(id);
            if (newSubtask.getEpicId() == epicId) {
                epicSubtasks.add(newSubtask);
            }
        }
        return epicSubtasks;
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> epicSubtasks = printAllSubtasksForEpic(epicId);

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


