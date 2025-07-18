package test.service;

import manager.CSVFormatter;
import org.junit.jupiter.api.Test;
import task.*;
import static org.junit.jupiter.api.Assertions.*;

class CSVFormatterTest {

    @Test
    void testToStringConverter_Task() {
        Task task = new Task(1, "Task 1", TaskStatus.NEW, "Description");
        String expected = "1,TASK,Task 1,NEW,Description";
        assertEquals(expected, CSVFormatter.toStringConverter(task));
    }

    @Test
    void testToStringConverter_SubTask() {
        SubTask subTask = new SubTask(2, "SubTask 1", TaskStatus.IN_PROGRESS, "Description", 1);
        String expected = "2,SUBTASK,SubTask 1,IN_PROGRESS,Description,1";
        assertEquals(expected, CSVFormatter.toStringConverter(subTask));
    }

    @Test
    void testToTaskConverter_Task() {
        String line = "1,TASK,Task 1,NEW,Description";
        Task task = CSVFormatter.toTaskConverter(line);
        assertEquals(1, task.getId());
        assertEquals("Task 1", task.getName());
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    void testToTaskConverter_SubTask() {
        String line = "2,SUBTASK,SubTask 1,IN_PROGRESS,Description,1";
        SubTask subTask = (SubTask) CSVFormatter.toTaskConverter(line);
        assertEquals(1, subTask.getEpicId());
    }

    @Test
    void testToTaskConverter_InvalidLine() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormatter.toTaskConverter(""));
        assertThrows(IllegalArgumentException.class, () -> CSVFormatter.toTaskConverter("1,TASK"));
    }
}
