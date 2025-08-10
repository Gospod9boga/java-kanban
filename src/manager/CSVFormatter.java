package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVFormatter {
    public static String getHeader() {
        return "id,type,name,status,description,duration,startTime,epic";
    }

    public static String toStringConverter(Task task) {
        if (task.getType() == null) {
            throw new IllegalStateException("Task type cannot be null");
        }
        StringBuilder sb = new StringBuilder();

        sb.append(task.getId()).append(",")
                .append(task.getType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",");

        Duration duration = task.getDuration();
        sb.append(duration != null ? duration.toMinutes() : "").append(",");

        LocalDateTime startTime = task.getStartTime();
        sb.append(startTime != null ? startTime.toString() : "").append(",");

        if (task.getType() == TaskType.SUBTASK) {
            sb.append(((SubTask) task).getEpicId());
        } else {
            sb.append("");
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
                if (parts.length < 8) {
                    throw new IllegalArgumentException("Для подзадачи недостаточно данных: " + line);
                }

                // Проверка epicId
                String epicIdStr = parts[7].trim();
                if (epicIdStr.isEmpty()) {
                    throw new IllegalArgumentException("Пустой epicId для подзадачи: " + line);
                }
                int epicId = Integer.parseInt(epicIdStr);

                // Проверка duration
                Duration duration = Duration.ZERO;
                if (parts.length > 5 && !parts[5].trim().isEmpty()) {
                    duration = Duration.ofMinutes(Long.parseLong(parts[5].trim()));
                }

                // Проверка startTime
                LocalDateTime startTime = null;
                if (parts.length > 6 && !parts[6].trim().isEmpty()) {
                    startTime = LocalDateTime.parse(parts[6].trim());
                }

                return new SubTask(id, name, status, description, duration, startTime, epicId);

            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

}
