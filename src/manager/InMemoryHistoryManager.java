package manager;
import task.Task;
import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private final ModifiedLinkedList historyList = new ModifiedLinkedList();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int taskId) {
        if (historyList.idToNode.containsKey(taskId)) {
            historyList.removeNode(historyList.idToNode.get(taskId));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTask();
    }

    private class ModifiedLinkedList {
        private Node head;
        private Node tail;
        private Map<Integer, Node> idToNode = new HashMap<>();

        public void linkLast(Task task) {
            int taskId = task.getId();
            if (idToNode.containsKey(taskId)) {
                remove(taskId);
            }
            Node newNode = new Node(task);

            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.setNext(newNode);
                newNode.setPrevious(tail);
                tail = newNode;
            }
            idToNode.put(taskId, newNode);

        }

        public void removeNode(Node node) {
            if (node.getPrevious() != null) {
                node.getPrevious().setNext(node.getNext());
            } else {
                head = node.getNext();
            }
            if (node.getNext() != null) {
                node.getNext().setPrevious(node.getPrevious());
            } else {
                tail = node.getPrevious();
            }
            idToNode.remove(node.getTask().getId());
        }

        public List<Task> getTask() {
            List<Task> history = new ArrayList<>();
            Node current = head;
            while (current != null) {
                history.add(current.getTask());
                current = current.getNext();
            }
            return history;
        }
    }
}

