package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void updateTimeFields(List<SubTask> subTasks) {
        if (subTasks == null || subTasks.isEmpty()) {
            this.setStartTime(null);
            this.setDuration(Duration.ZERO);
            return;
        }

        LocalDateTime earliestStart = null;
        Duration totalDuration = Duration.ZERO;

        for (SubTask sub : subTasks) {
            LocalDateTime subStart = sub.getStartTime();
            Duration subDuration = sub.getDuration();

            if (subStart != null) {
                if (earliestStart == null || subStart.isBefore(earliestStart)) {
                    earliestStart = subStart;
                }
            }

            if (subDuration != null) {
                totalDuration = totalDuration.plus(subDuration);
            }
        }

        this.setStartTime(earliestStart);
        this.setDuration(totalDuration);
    }


    public LocalDateTime getEndTime() {
        if (this.getStartTime() == null || this.getDuration() == null) {
            return null;
        }
        return this.getStartTime().plus(this.getDuration());
    }

}

