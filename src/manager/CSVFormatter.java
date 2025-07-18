package manager;

import task.*;

public class CSVFormatter {
    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toStringConverter(Task task) {
        if (task.getType() == null) {
            throw new IllegalStateException("Task type cannot be null");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",").append(task.getType()).append(",").
                append(task.getName()).append(",").append(task.getStatus()).
                append(",").append(task.getDescription());
        if (task.getType() == TaskType.SUBTASK) {
            sb.append(",").append(((SubTask) task).getEpicId());
        }
        return sb.toString();
    }

    public static Task toTaskConverter(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустая строка");
        }

        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Недостаточно данных в строке: " + line);
        }

        int id = Integer.parseInt(parts[0].trim());
        TaskType type = TaskType.valueOf(parts[1].trim());
        String name = parts[2].trim();
        TaskStatus status = TaskStatus.valueOf(parts[3].trim());
        String description = parts[4].trim();

        switch (type) {
            case TASK:
                return new Task(id, name, status, description);

            case EPIC:
                return new Epic(id, name, status, description);

            case SUBTASK:
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Для подзадачи отсутствует epicId: " + line);
                }
                int epicId = Integer.parseInt(parts[5].trim());
                return new SubTask(id, name, status, description, epicId);

            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}
