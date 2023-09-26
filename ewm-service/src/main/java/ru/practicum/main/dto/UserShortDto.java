package ru.practicum.main.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
public class UserShortDto {
    Long id;

    @NotBlank
    String name;
}
