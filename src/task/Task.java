package task;

public class Task {
    private int id;
    private TaskType type;
    private String name;
    private TaskStatus status;
    private String description;



    public Task(int id, String name, TaskStatus status, String description) {
        this.id = id;
        this.type = TaskType.TASK;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public  Task(){

    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }
    // сеттер для name
    public void setName(String name) {
        this.name = name;
    }

    // геттер для  name
    public String getName() {
        return this.name;
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
                ", название='" + name + '\'' +
                ", описание='" + description + '\'' +
                ", статус=" + status +
                '}';
    }
}




