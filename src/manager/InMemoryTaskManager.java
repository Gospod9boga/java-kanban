package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;
    private final Comparator<Task> taskComparator = (t1, t2) -> {
        LocalDateTime s1 = t1.getStartTime();
        LocalDateTime s2 = t2.getStartTime();

        if (s1 == null && s2 == null) {
            return Integer.compare(t1.getId(), t2.getId());
        }
        if (s1 == null) {
            return 1;
        }
        if (s2 == null) {
            return -1;
        }
        int cmp = s1.compareTo(s2);
        if (cmp == 0) {
            return Integer.compare(t1.getId(), t2.getId());
        }
        return cmp;
    };
    protected TreeSet<Task> taskSet = new TreeSet<>(taskComparator);


    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    //создание задачи
    @Override
    public void addTask(Task task) {
        if (task.getStartTime() != null && task.getDuration() != null) {
            boolean hasOverlap = taskSet.stream()
                    .anyMatch(existingTask -> isOverlap(existingTask, task));

            if (hasOverlap) {
                throw new IllegalArgumentException("Задача пересекается с другой задачей");
            }
        }

        task.setId(nextId++);
        taskSet.add(task);
        tasks.put(task.getId(), task);
    }

    // Получение списка всех задач
    @Override
    public List<Task> getAllTask() {
        return new ArrayList<Task>(tasks.values());
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            historyManager.add(oldTask);
            boolean hasOverlap = taskSet.stream()
                    .filter(existingTask -> !existingTask.equals(oldTask))
                    .anyMatch(existingTask -> isOverlap(existingTask, task));

            if (hasOverlap) {
                throw new IllegalArgumentException("Обновленная задача пересекается с другой задачей");
            }

            tasks.put(task.getId(), task);
            taskSet.remove(oldTask);
            taskSet.add(task);
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
            historyManager.add(task);
        }
        return tasks.get(id);
    }

    // удаление по id задачи
    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            taskSet.remove(task);
        }
    }

    //создание Эпика
    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);

        if (epic.getStartTime() != null && epic.getDuration() != null) {
            taskSet.add(epic);
        }
    }


    // Получение списка всех эпиков
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<Epic>(epics.values());
    }

    // Удаление всех эпиков
    @Override
    public void clearAllEpic() {
        epics.values().stream()
                .flatMap(epic -> epic.getSubTaskIds().stream())
                .map(subtasks::get)
                .filter(subtask -> subtask != null)
                .forEach(taskSet::remove);

        epics.values().forEach(taskSet::remove);

        subtasks.clear();
        epics.clear();
    }


    // получение по id эпика
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epics.get(id);
    }

    // удаление по id эпика
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubTaskIds().stream()
                    .map(subtasks::remove)
                    .filter(subtask -> subtask != null)
                    .forEach(taskSet::remove);

            epics.remove(id);
            taskSet.remove(epic);
        } else {
            System.out.println("Эпик с ID " + id + " не найден.");
        }
    }

    // Обновление эпика
    @Override
    public void updateTask(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Эпик с ID " + epic.getId() + " не найден");
            return;
        }

        Epic oldEpic = epics.get(epic.getId());
        epics.put(epic.getId(), epic);
        epic.updateTimeFields(getAllSubtasksForEpic(epic.getId()));
        taskSet.remove(oldEpic);

        boolean hasOverlap = taskSet.stream()
                .anyMatch(t -> isOverlap(t, epic));

        if (hasOverlap) {
            epics.put(oldEpic.getId(), oldEpic);
            taskSet.add(oldEpic);
            throw new IllegalArgumentException("Обновление эпика вызывает пересечение с другими задачами");
        }

        if (epic.getStartTime() != null && epic.getDuration() != null) {
            taskSet.add(epic);
        }
    }



    //создание подзадачи
    @Override
    public void addSubtask(SubTask subtask) {
        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            boolean hasOverlap = taskSet.stream()
                    .anyMatch(existingTask -> isOverlap(existingTask, subtask));

            if (hasOverlap) {
                throw new IllegalArgumentException("Подзадача пересекается с другой задачей");
            }
        }

        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubTaskIds().add(subtask.getId());
            updateEpicStatus(epic.getId());
            epic.updateTimeFields(getAllSubtasksForEpic(epic.getId()));

            if (epic.getStartTime() != null && epic.getDuration() != null) {
                taskSet.remove(epic);
                taskSet.add(epic);
            }
        } else {
            throw new IllegalArgumentException("Эпик с таким ID не найден");
        }

        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            taskSet.add(subtask);
        }
    }


    // Получение списка всех подзадач
    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<SubTask>(subtasks.values());
    }

    // Удаление всех подзадач
    @Override
    public void clearAllSubtask() {
        subtasks.values().stream()
                .peek(taskSet::remove)
                .forEach(subtask -> epics.values().forEach(epic -> epic.getSubTaskIds().remove(Integer.valueOf(subtask.getId()))));
        subtasks.clear();
        epics.values().forEach(epic -> updateEpicStatus(epic.getId()));
    }

    // получение по id подзадачи
    @Override
    public SubTask getSubtask(int id) {
        SubTask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtasks.get(id);
    }

    // удаление по id подзадачу
    @Override
    public void deleteSubtaskById(int id) {
        epics.values().stream()
                .filter(epic -> epic.getSubTaskIds().remove(Integer.valueOf(id)))
                .forEach(epic -> {
                    updateEpicStatus(epic.getId());
                    epic.updateTimeFields(getAllSubtasksForEpic(epic.getId()));
                });

        SubTask removedSubtask = subtasks.remove(id);
        if (removedSubtask != null) {
            taskSet.remove(removedSubtask);
        }
    }


    // Обновление подзадачи
    @Override
    public void updateTask(SubTask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            System.out.println("Подзадача с ID " + subtask.getId() + " не найдена");
            return;
        }

        SubTask oldSubtask = subtasks.get(subtask.getId());

        if (subtask.getStartTime() != null && subtask.getDuration() != null) {
            boolean hasOverlap = taskSet.stream()
                    .filter(t -> t.getId() != subtask.getId())
                    .anyMatch(existingTask -> isOverlap(existingTask, subtask));

            if (hasOverlap) {
                throw new IllegalArgumentException("Обновлённая подзадача пересекается с другой задачей");
            }
        }

        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic.getId());
            epic.updateTimeFields(getAllSubtasksForEpic(epic.getId()));

            taskSet.remove(oldSubtask);
            taskSet.add(subtask);

            taskSet.remove(epic);
            if (epic.getStartTime() != null && epic.getDuration() != null) {
                taskSet.add(epic);
            }
        }
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(taskSet);
    }

    public boolean isOverlap(Task t1, Task t2) {
        if (t1.getStartTime() == null || t1.getDuration() == null ||
                t2.getStartTime() == null || t2.getDuration() == null) {
                        return false;
        }

        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = start1.plus(t1.getDuration());

        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = start2.plus(t2.getDuration());

        return !(end1.isEqual(start2) || end1.isBefore(start2) || end2.isEqual(start1) || end2.isBefore(start1));
    }


}


