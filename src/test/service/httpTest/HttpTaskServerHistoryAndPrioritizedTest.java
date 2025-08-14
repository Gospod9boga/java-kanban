package test.service.httpTest;

import com.google.gson.Gson;
import http.server.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import task.TaskStatus;
import task.Task;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerHistoryAndPrioritizedTest {
    private HttpTaskServer server;
    private TaskManager manager;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        gson = server.getGson();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testGetEmptyHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body().trim());
    }

    @Test
    public void testGetHistoryWithTasks() throws IOException, InterruptedException {
        Task task1 = new Task(0, "Task 1", TaskStatus.NEW, "Description 1",
                Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        Task task2 = new Task(0, "Task 2", TaskStatus.NEW, "Description 2",
                Duration.ofMinutes(45), LocalDateTime.now().plusHours(3));

        String jsonTask1 = gson.toJson(task1);
        HttpRequest createRequest1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .build();
        client.send(createRequest1, HttpResponse.BodyHandlers.ofString());

        String jsonTask2 = gson.toJson(task2);
        HttpRequest createRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .build();
        client.send(createRequest2, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();
        client.send(getRequest1, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/2"))
                .GET()
                .build();
        client.send(getRequest2, HttpResponse.BodyHandlers.ofString());

        HttpRequest historyRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, historyResponse.statusCode());
        assertTrue(historyResponse.body().contains("Task 1") || historyResponse.body().contains("Task 2"));
    }

    @Test
    public void testGetEmptyPrioritizedList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body().trim());
    }

    @Test
    public void testGetPrioritizedTasksWithTasks() throws IOException, InterruptedException {
        Task task1 = new Task(0, "Task 1", TaskStatus.NEW, "Description 1",
                Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 1, 14, 0));
        Task task2 = new Task(0, "Task 2", TaskStatus.NEW, "Description 2",
                Duration.ofMinutes(45), LocalDateTime.of(2025, 1, 1, 10, 0));
        Task task3 = new Task(0, "Task 3", TaskStatus.NEW, "Description 3",
                Duration.ofMinutes(60), LocalDateTime.of(2025, 1, 1, 12, 0));

        String jsonTask1 = gson.toJson(task1);
        HttpRequest createRequest1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .build();
        client.send(createRequest1, HttpResponse.BodyHandlers.ofString());

        String jsonTask2 = gson.toJson(task2);
        HttpRequest createRequest2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .build();
        client.send(createRequest2, HttpResponse.BodyHandlers.ofString());

        String jsonTask3 = gson.toJson(task3);
        HttpRequest createRequest3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask3))
                .build();
        client.send(createRequest3, HttpResponse.BodyHandlers.ofString());

        HttpRequest prioritizedRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> prioritizedResponse = client.send(prioritizedRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, prioritizedResponse.statusCode());

        assertTrue(prioritizedResponse.body().contains("Task 1"));
        assertTrue(prioritizedResponse.body().contains("Task 2"));
        assertTrue(prioritizedResponse.body().contains("Task 3"));
    }

    @Test
    public void testGetPrioritizedTasksWithoutStartTime() throws IOException, InterruptedException {
        Task taskWithoutTime = new Task(0, "Task without time", TaskStatus.NEW, "Description");
        String taskJson = gson.toJson(taskWithoutTime);
        HttpRequest createTaskRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(createTaskRequest, HttpResponse.BodyHandlers.ofString());

        Task taskWithTime = new Task(0, "Task with time", TaskStatus.NEW, "Description",
                Duration.ofMinutes(30), LocalDateTime.of(2025, 1, 4, 15, 0));
        String taskWithTimeJson = gson.toJson(taskWithTime);
        HttpRequest createTaskWithTimeRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskWithTimeJson))
                .build();
        client.send(createTaskWithTimeRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest prioritizedRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> prioritizedResponse = client.send(prioritizedRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, prioritizedResponse.statusCode());
        assertTrue(prioritizedResponse.body().length() >= 2); // Как минимум "[]" или содержимое
    }
}
