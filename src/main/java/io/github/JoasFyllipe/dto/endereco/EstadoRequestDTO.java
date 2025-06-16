package io.github.JoasFyllipe.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadoRequestDTO(
        @NotBlank(message = "O nome do estado é obrigatório")
        @Size(min = 2, max = 60)
        String nome,

        @NotBlank(message = "A sigla do estado é obrigatória")
        @Size(min = 2, max = 2, message = "A sigla deve ter 2 caracteres")
        String sigla
) {}