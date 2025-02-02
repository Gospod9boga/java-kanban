package task;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    // Геттер для subTaskIds
    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    // Сеттер для subTaskIds
    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }
}