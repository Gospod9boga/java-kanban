package task;

public class SubTask extends Task {
    private int epicId;


    public SubTask(int id, String name, TaskStatus status, String description, int epicId) {
        super(id, name, status, description);
        this.setType(TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, TaskStatus status, String description) {
        super(id, name, status, description);
    }

    public SubTask() {

    }

    // Сеттер для epicId
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // Геттер для epicId
    public int getEpicId() {
        return epicId;
    }
}
