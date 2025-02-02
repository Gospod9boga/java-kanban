package manager;
import task.Task;

class Node {
    Node previous;
    Node next;
    Task task;

    Node(Task task) {
        this.task = task;
        this.next = null;
        this.previous = null;
    }

    void setTask(Task task) {
        this.task = task;
    }

    Task getTask() {
        return task;
    }

    void setNext(Node next) {
        this.next = next;
    }

    Node getNext() {
        return next;
    }

    void setPrevious(Node previous) {
        this.previous = previous;
    }

    Node getPrevious() {
        return previous;
    }
}
