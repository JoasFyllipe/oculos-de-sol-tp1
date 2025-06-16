package io.github.JoasFyllipe.dto.oculos.patch;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OculosUpdatePrecoDTO(
        @NotNull(message = "O preço não pode ser nulo.")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
        BigDecimal novoPreco
) {}