package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals(GET)) {
            sendResponse(exchange, 405, "text/plain; charset=UTF-8", "Метод не поддерживается");
            return;
        }

        List<Task> history = manager.getHistory();
        sendTextOk(exchange, history);
    }
}
