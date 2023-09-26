package ru.practicum.main.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotEmpty
    @NotBlank
    @Size(max = 50)
    private String name;
}
