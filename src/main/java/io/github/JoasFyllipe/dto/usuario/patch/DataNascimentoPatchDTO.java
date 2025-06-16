package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record DataNascimentoPatchDTO(
        @NotBlank(message = "O campo data de nascimento deve ser informado")
        LocalDate dataNascimento
) {
}
