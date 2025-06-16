package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SenhaPatchRequestDTO(
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        String senhaAtual,
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        @Size(min = 6, max = 20, message = "A senha deve ter no minimo 6 caracteres e no máximo 20 caracteres")
        String novaSenha,
        @NotBlank(message = "A senha não pode ser nula ou vazia")
        @Size(min = 6, max = 20, message = "A senha deve ter no minimo 6 caracteres e no máximo 20 caracteres")
        String repetirNovaSenha
) {
}
