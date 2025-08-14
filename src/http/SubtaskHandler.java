package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import manager.TaskManager;
import task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        boolean hasId = pathParts.length > 2;

        if (method.equals("GET")) {
            if (hasId) {
                try {
                    int id = Integer.parseInt(pathParts[2]);
                    SubTask subtask = manager.getSubtask(id);
                    if (subtask != null) {
                        sendTextOk(exchange, subtask);
                    } else {
                        sendIdNotFound(exchange, id);
                    }
                } catch (NumberFormatException e) {
                    sendIncorrectId(exchange, pathParts[2]);
                }
            } else {
                List<SubTask> subtasks = manager.getAllSubtasks();
                sendTextOk(exchange, subtasks);
            }
        } else if (method.equals("POST")) {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes());
            SubTask subtask = gson.fromJson(body, SubTask.class);
            if (subtask.getId() == 0) {
                try {
                    manager.addSubtask(subtask);
                    String response = "Подзадача успешно создана";
                    sendTextCreatedOk(exchange, response);
                } catch (IllegalArgumentException e) {
                    sendHasOverlaps(exchange);
                }
            } else {
                try {
                    manager.updateTask(subtask);
                    sendTextOk(exchange, "Подзадача успешно обновлена");
                } catch (IllegalArgumentException e) {
                    sendHasOverlaps(exchange);
                }
            }
        } else if (method.equals("DELETE")) {
            if (hasId) {
                try {
                    int id = Integer.parseInt(pathParts[2]);
                    manager.deleteSubtaskById(id);
                    sendTextOk(exchange, "Подзадача успешно удалена");
                } catch (NumberFormatException e) {
                    sendIncorrectId(exchange, pathParts[2]);
                }
            }
        }
    }
}
