package test.service;

import org.junit.jupiter.api.Test;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskOverlapTest {

    public boolean isOverlap(Task t1, Task t2) {
        if (t1.getStartTime() == null || t1.getDuration() == null ||
                t2.getStartTime() == null || t2.getDuration() == null) {
            return false;
        }

        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = start1.plus(t1.getDuration());

        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = start2.plus(t2.getDuration());

        return !(end1.isEqual(start2) || end1.isBefore(start2) || end2.isEqual(start1) || end2.isBefore(start1));
    }

    @Test
    void testNoOverlapDifferentIntervals() {
        Task t1 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), Duration.ofMinutes(30));
        Task t2 = createTask(LocalDateTime.of(2023, 7, 17, 11, 0), Duration.ofMinutes(30));
        assertFalse(isOverlap(t1, t2));
    }

    @Test
    void testOverlapPartial() {
        Task t1 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), Duration.ofMinutes(60));
        Task t2 = createTask(LocalDateTime.of(2023, 7, 17, 10, 30), Duration.ofMinutes(60));
        assertTrue(isOverlap(t1, t2));
    }

    @Test
    void testOverlapExactMatch() {
        Task t1 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), Duration.ofMinutes(30));
        Task t2 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), Duration.ofMinutes(30));
        assertTrue(isOverlap(t1, t2));
    }

    @Test
    void testNoOverlapAdjacentIntervals() {
        Task t1 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), Duration.ofMinutes(30));
        Task t2 = createTask(LocalDateTime.of(2023, 7, 17, 10, 30), Duration.ofMinutes(30));
        assertFalse(isOverlap(t1, t2));
    }

    @Test
    void testNullStartOrDuration() {
        Task t1 = createTask(null, Duration.ofMinutes(30));
        Task t2 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), Duration.ofMinutes(30));
        assertFalse(isOverlap(t1, t2));

        Task t3 = createTask(LocalDateTime.of(2023, 7, 17, 10, 0), null);
        assertFalse(isOverlap(t3, t2));
    }

    private Task createTask(LocalDateTime start, Duration duration) {
        Task task = new Task();
        task.setStartTime(start);
        task.setDuration(duration);
        return task;
    }
}

