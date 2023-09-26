package ru.practicum.main.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 2)
    @Size(max = 250)
    String name;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 6)
    @Size(max = 254)
    String email;


}
