package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        boolean hasId = pathParts.length > 2;

        if (method.equals(GET)) {
            if (hasId) {
                try {
                    int id = Integer.parseInt(pathParts[2]);
                    Task task = manager.getTask(id);
                    if (task != null) {
                        sendTextOk(exchange, task);
                    } else {
                        sendIdNotFound(exchange, id);
                    }
                } catch (NumberFormatException e) {
                    sendIncorrectId(exchange, pathParts[2]);
                }
            } else {
                List<Task> tasks = manager.getAllTask();
                sendTextOk(exchange, tasks);
            }
        } else if (method.equals(POST)) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes());
            Task task = GSON.fromJson(body, Task.class);
            if (task.getId() == 0) {
                try {
                    manager.addTask(task);
                    String response = "Задача успешно создана";
                    sendTextCreatedOk(exchange, response);
                } catch (IllegalArgumentException e) {
                    sendHasOverlaps(exchange);
                }
            } else {
                try {
                    manager.updateTask(task);
                    String response = "Задача успешно обновлена";
                    sendTextOk(exchange, response);
                } catch (IllegalArgumentException e) {
                    sendHasOverlaps(exchange);
                }
            }
        } else if (method.equals(DELETE)) {
            int id = Integer.parseInt(pathParts[2]);
            try {
                manager.deleteTaskById(id);
                String response = "Задача успешно удалена";
                sendTextOk(exchange, response);
            } catch (NumberFormatException e) {
                sendIdNotFound(exchange, id);
            }
        }
    }
}

