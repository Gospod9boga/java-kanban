package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;
import task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        try {
            switch (method) {
                case GET:
                    handleGetRequest(exchange, pathParts);
                    break;
                case POST:
                    handlePostRequest(exchange);
                    break;
                case DELETE:
                    handleDeleteRequest(exchange, pathParts);
                    break;
                default:
                    sendResponse(exchange, 405, "text/plain; charset=UTF-8", "Метод не поддерживается");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "text/plain; charset=UTF-8", "Внутренняя ошибка сервера");
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<Epic> epics = manager.getAllEpics();
            sendTextOk(exchange, epics);
        } else if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Epic epic = manager.getEpic(id);
                if (epic != null) {
                    sendTextOk(exchange, epic);
                } else {
                    sendIdNotFound(exchange, id);
                }
            } catch (NumberFormatException e) {
                sendIncorrectId(exchange, pathParts[2]);
            }
        } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                Epic epic = manager.getEpic(id);
                if (epic != null) {
                    List<SubTask> subtasks = manager.getAllSubtasksForEpic(id);
                    sendTextOk(exchange, subtasks);
                } else {
                    sendIdNotFound(exchange, id);
                }
            } catch (NumberFormatException e) {
                sendIncorrectId(exchange, pathParts[2]);
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String body = new String(inputStream.readAllBytes());
            Epic epic = GSON.fromJson(body, Epic.class);
            manager.addEpic(epic);
            sendTextCreatedOk(exchange, "Epic успешно создан");
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            try {
                int id = Integer.parseInt(pathParts[2]);
                manager.deleteEpicById(id);
                sendTextOk(exchange, "Epic успешно удален");
            } catch (NumberFormatException e) {
                sendIncorrectId(exchange, pathParts[2]);
            }
        }
    }
}