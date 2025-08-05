package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(int id, String name, TaskStatus status, String description, ArrayList<Integer> subTaskIds) {
        super(id, name, status, description);
        this.subTaskIds = subTaskIds;
    }

    public Epic(int id, String name, TaskStatus status, String description) {
        super(id, name, status, description);
        this.setType(TaskType.EPIC);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic() {

    }


    // Геттер для subTaskIds
    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    // Сеттер для subTaskIds
    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }
}