package io.github.JoasFyllipe.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(

        @NotBlank(message = "O campo mail deve ser informado")
        String email,
        @NotBlank(message = "O campo senha deve ser informado")
        String senha
) {
}
