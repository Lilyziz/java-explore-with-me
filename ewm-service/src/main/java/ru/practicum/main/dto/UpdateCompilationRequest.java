package ru.practicum.main.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    List<Long> events;
    Boolean pinned = false;

    @Size(min = 1)
    @Size(max = 50)
    String title;
}
