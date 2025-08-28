package ch.iict.training.api;

import ch.iict.training.dto.TaskIn;
import ch.iict.training.dto.TaskOut;
import ch.iict.training.entities.Task;

public final class TaskMapper {
    private TaskMapper() {
    }

    public static TaskOut toOut(Task t) {
        return new TaskOut(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.isDone(),
                t.getDueDate()
        );
    }

    public static Task toNewEntity(TaskIn in) {
        Task t = new Task();
        t.setTitle(in.getTitle());
        t.setDescription(in.getDescription());
        t.setDone(in.getDone() != null ? in.getDone() : false);
        t.setDueDate(in.getDueDate());
        return t;
    }

    public static void updateEntity(Task t, TaskIn in) {
        if (in.getTitle() != null) t.setTitle(in.getTitle());
        if (in.getDescription() != null) t.setDescription(in.getDescription());
        if (in.getDone() != null) t.setDone(in.getDone());
        if (in.getDueDate() != null) t.setDueDate(in.getDueDate());
    }
}

