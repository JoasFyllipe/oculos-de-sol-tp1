package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.Email;

public record EmailPatchRequestDTO(
        @Email
        String novoEmail
) {
}
