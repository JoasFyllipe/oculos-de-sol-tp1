package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.NotBlank;

public record NomePatchRequestDTO(
        @NotBlank(message = "O nome não pode ser nulo ou vazio")
        String novoNome
) {
}
