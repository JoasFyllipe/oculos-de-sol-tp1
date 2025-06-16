package io.github.JoasFyllipe.dto.marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MarcaRequestDTO(
        @NotBlank(message = "O nome da marca não pode estar vazio.")
        @Size(max = 60, message = "O nome da marca deve ter no máximo 60 caracteres.")
        String nome
) {}