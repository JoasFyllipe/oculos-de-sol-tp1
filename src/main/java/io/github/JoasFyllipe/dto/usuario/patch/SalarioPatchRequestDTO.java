package io.github.JoasFyllipe.dto.usuario.patch;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SalarioPatchRequestDTO(
        @NotNull
        BigDecimal salario
) {
}
