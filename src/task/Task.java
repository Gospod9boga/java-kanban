package task;

public class Task {
    private String title;
    private String description;
    private int id;
    private TaskStatus status;

    // сеттер для  title
    public void setTitle(String title) {
        this.title = title;
    }

    // геттер для  title
    public String getTitle() {
        return this.title;
    }

    // сеттер для  description
    public void setDescription(String description) {
        this.description = description;
    }

    // геттер для description
    public String getDescription() {
        return this.description;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return this.id;
    }
    // Геттер для статуса
    public TaskStatus getStatus() {
        return status;
    }

    // Сеттер для статуса
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Задача{" +
                "ID=" + id +
                ", название='" + title + '\'' +
                ", описание='" + description + '\'' +
                ", статус=" + status +
                '}';
    }
}




