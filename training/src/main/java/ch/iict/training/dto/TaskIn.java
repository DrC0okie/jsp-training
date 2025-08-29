package ch.iict.training.dto;

import ch.iict.training.api.validation.ExcludedDaysOfMonth;
import ch.iict.training.api.validation.ForbidSubstring;
import ch.iict.training.api.validation.TitleStartsWithCapital;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TaskIn {
    @NotBlank
    @Size(max = 100, min = 2)
    @TitleStartsWithCapital(message = "title must start with a capitalized word")
    @ForbidSubstring(substring = "banana", ignoreCase = true, message = "title must not contain the word 'banana'")
    private String title;

    @Size(max = 300)
    @ForbidSubstring(substring = "kiwi", ignoreCase = true, message = "title must not contain the word 'kiwi'")
    private String description;

    private Boolean done;

    @FutureOrPresent
    @ExcludedDaysOfMonth(value = { 1, 15 }, message = "dueDate cannot be the 1st or the 15th of a month")
    private LocalDate dueDate;

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

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
