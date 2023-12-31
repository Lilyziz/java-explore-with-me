package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class NewCompilationDto {
    List<Long> events;
    Boolean pinned = false;

    @Size(min = 1)
    @Size(max = 50)
    @NonNull
    @NotBlank
    String title;
}
