package ch.iict.training.dto;

import java.time.LocalDate;

public record TaskOut(
        Long id,
        String title,
        String description,
        boolean done,
        LocalDate dueDate
) {}