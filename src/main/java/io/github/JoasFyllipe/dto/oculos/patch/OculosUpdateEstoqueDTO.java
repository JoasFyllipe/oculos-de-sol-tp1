package io.github.JoasFyllipe.dto.oculos.patch;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OculosUpdateEstoqueDTO(
        @NotNull
        @Min(value = 0)
        Integer novaQuantidade
) {}