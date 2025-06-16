package io.github.JoasFyllipe.dto.itempedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(
        @NotNull(message = "O ID do óculos não pode ser nulo.")
        Long idOculos,

        @NotNull(message = "A quantidade não pode ser nula.")
        @Positive(message = "A quantidade deve ser um número positivo.")
        Integer quantidade
) {}