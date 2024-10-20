package task;

public class SubTask extends Task {
    private int epicId;

    // Сеттер для epicId
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    // Геттер для epicId
    public int getEpicId() {
        return epicId;
    }
}
