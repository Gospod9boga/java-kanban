package test.service;

import manager.CSVFormatter;
import org.junit.jupiter.api.Test;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatterTest {

    @Test
    void testToStringConverter_Task() {
        LocalDateTime startTime = LocalDateTime.of(2023, 7, 17, 10, 0);
        Task task = new Task(1, "Task 1", TaskStatus.NEW, "Description", Duration.ofMinutes(30), startTime);
        String expected = "1,TASK,Task 1,NEW,Description,30,2023-07-17T10:00,";
        assertEquals(expected, CSVFormatter.toStringConverter(task));
    }

    @Test
    void testToStringConverter_SubTask() {
        LocalDateTime startTime = LocalDateTime.of(2023, 7, 17, 11, 0);
        SubTask subTask = new SubTask(2, "SubTask 1", TaskStatus.IN_PROGRESS, "Description",
                Duration.ofMinutes(45), startTime, 1);
        String expected = "2,SUBTASK,SubTask 1,IN_PROGRESS,Description,45,2023-07-17T11:00,1";
        assertEquals(expected, CSVFormatter.toStringConverter(subTask));
    }

    @Test
    void testToTaskConverter_SubTask() {
        String line = "2,SUBTASK,SubTask 1,IN_PROGRESS,Description,45,2023-07-17T11:00,1";
        SubTask subTask = (SubTask) CSVFormatter.toTaskConverter(line);
        assertEquals(2, subTask.getId());
        assertEquals("SubTask 1", subTask.getName());
        assertEquals(TaskStatus.IN_PROGRESS, subTask.getStatus());
        assertEquals(Duration.ofMinutes(45), subTask.getDuration());
        assertEquals(LocalDateTime.of(2023, 7, 17, 11, 0), subTask.getStartTime());
        assertEquals(1, subTask.getEpicId());
    }

    @Test
    void testToTaskConverter_InvalidLine() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormatter.toTaskConverter(""));
        assertThrows(IllegalArgumentException.class, () -> CSVFormatter.toTaskConverter("1,TASK"));
    }
}
