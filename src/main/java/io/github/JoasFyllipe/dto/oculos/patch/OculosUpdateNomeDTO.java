package io.github.JoasFyllipe.dto.oculos.patch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OculosUpdateNomeDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        @Size(max = 60, message = "O nome não pode exceder 60 caracteres.")
        String novoNome
) {}