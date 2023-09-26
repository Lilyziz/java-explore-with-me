package ru.practicum.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HitDto {
    private String app;
    private String uri;
    private Integer hit;
}
