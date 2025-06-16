package io.github.JoasFyllipe.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CidadeRequestDTO(
        @NotBlank(message = "O nome da cidade é obrigatório")
        @Size(max = 100)
        String nome,

        @NotNull(message = "O ID do estado é obrigatório")
        Long estadoId
) {}