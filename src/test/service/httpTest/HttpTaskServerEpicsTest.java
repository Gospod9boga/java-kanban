package test.service.httpTest;

import com.google.gson.Gson;
import http.BaseHttpHandler;
import http.server.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.TaskStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpTaskServerEpicsTest {
    private HttpTaskServer server;
    private TaskManager manager;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        gson = BaseHttpHandler.GSON;
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void     testGetEmptyEpicsList() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body().trim());
    }

    @Test
    public void testCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", TaskStatus.NEW, "Testing epic", new ArrayList<>());
        String json = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("успешно создан"),
                "Ответ должен содержать сообщение об успешном создании");

        assertEquals("Epic успешно создан", response.body());
    }

    @Test
    public void  testGetAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic(0, "Epic1", TaskStatus.NEW, "Description1", new ArrayList<>());
        Epic epic2 = new Epic(0, "Epic2", TaskStatus.NEW, "Description2", new ArrayList<>());

        String jsonEpic1 = gson.toJson(epic1);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic1))
                .build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());

        String jsonEpic2 = gson.toJson(epic2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic2))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Epic1"));
        assertTrue(response.body().contains("Epic2"));
    }

    @Test
    public void testEpicNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/999"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", TaskStatus.NEW, "Testing epic", new ArrayList<>());
        String json = gson.toJson(epic);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResponse.statusCode());

        assertEquals("Epic успешно создан", createResponse.body().trim());

        HttpRequest getAllRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();
        HttpResponse<String> getAllResponse = client.send(getAllRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getAllResponse.statusCode());

        Epic[] allEpics = gson.fromJson(getAllResponse.body(), Epic[].class);
        assertTrue(allEpics.length > 0, "Не найдено ни одного эпика");
        int epicId = allEpics[allEpics.length - 1].getId(); // Берем последний созданный

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epicId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, deleteResponse.statusCode());
        assertEquals("Epic успешно удален", deleteResponse.body().trim());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epicId))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    public void testGetEpicSubtasksNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/999/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGetEpicWithInvalidId() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/abc"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("Некорректный id"));
    }
}