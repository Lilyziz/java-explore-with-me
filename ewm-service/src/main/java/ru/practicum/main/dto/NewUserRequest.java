package ru.practicum.main.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewUserRequest {
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 6, max = 254)
    String email;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 250)
    String name;
}
