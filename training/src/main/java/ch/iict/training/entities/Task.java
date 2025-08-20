package ch.iict.training.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private boolean done;
    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate date) {
        this.dueDate = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Transient
    public String getUrgencyClass() {
        if (dueDate == null) return "due-none";
        LocalDate today = LocalDate.now();
        if (dueDate.isBefore(today)) return "overdue";
        long days = ChronoUnit.DAYS.between(today, dueDate);
        if (days <= 3) return "due-soon";
        return "due-later";
    }
}

