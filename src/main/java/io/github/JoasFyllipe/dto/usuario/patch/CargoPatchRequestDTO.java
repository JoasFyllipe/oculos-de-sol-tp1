package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.NotBlank;

public record CargoPatchRequestDTO(

        @NotBlank(message = "O cargo deve ser informado")
        String cargo
) {
}
