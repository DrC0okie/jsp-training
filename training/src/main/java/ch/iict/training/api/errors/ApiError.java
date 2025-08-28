package ch.iict.training.api.errors;

import java.util.List;

public record ApiError(String type, String message, List<FieldError> errors) {
    public record FieldError(String field, String message) {}
    public static ApiError of(String type, String message) {
        return new ApiError(type, message, List.of());
    }
}
