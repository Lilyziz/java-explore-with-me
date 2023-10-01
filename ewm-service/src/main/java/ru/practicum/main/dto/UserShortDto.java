package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
@Builder
@AllArgsConstructor
public class UserShortDto {
    Long id;

    @NotBlank
    String name;
}
