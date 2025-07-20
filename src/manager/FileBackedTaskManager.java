package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        int maxId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                Task task = CSVFormatter.toTaskConverter(line);

                if (task.getId() > maxId) {
                    maxId = task.getId();
                }


                if (task instanceof Epic) {
                    taskManager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    taskManager.addSubtask((SubTask) task);
                } else {
                    taskManager.addTask(task);
                }
            }


            taskManager.setNextId(maxId + 1);

        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден: " + file.getPath(), e);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }

        return taskManager;
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        if (task != null) {
            save();
        }
        return task;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void clearAllEpic() {
        super.clearAllEpic();
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        if (epic != null) {
            save();
        }
        return epic;
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateTask(Epic epic) {
        super.updateTask(epic);
        save();
    }

    @Override
    public void addSubtask(SubTask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void clearAllSubtask() {
        super.clearAllSubtask();
        save();
    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask subTask = super.getSubtask(id);
        if (subTask != null) {
            save();
        }
        return subTask;
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void updateTask(SubTask subtask) {
        super.updateTask(subtask);
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(file)))) {
            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            for (Task task : getAllTask()) {
                writer.write(CSVFormatter.toStringConverter(task));
                writer.newLine();
            }

            for (Epic epic : getAllEpics()) {
                writer.write(CSVFormatter.toStringConverter(epic));
                writer.newLine();
            }

            for (SubTask subTask : getAllSubtasks()) {
                writer.write(CSVFormatter.toStringConverter(subTask));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в сохранении данных", e);
        }

    }
}
