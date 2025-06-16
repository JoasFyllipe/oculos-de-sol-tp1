package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.NotBlank;

public record CpfPatchRequestDTO(

        @NotBlank(message = "O campo CPF deve ser informado")
        String novoCpf
) {
}
