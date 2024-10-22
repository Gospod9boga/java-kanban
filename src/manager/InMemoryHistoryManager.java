package manager;
import task.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history;
    private final int maxSize;

    public InMemoryHistoryManager(int maxSize) {
        this.history = new LinkedList<>();
        this.maxSize = maxSize;
    }


    @Override
    public void add(Task task) {
        history.remove(task);
        if (history.size() == maxSize) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }
}
