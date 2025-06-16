package io.github.JoasFyllipe.dto.oculos.patch;

import jakarta.validation.constraints.NotNull;

public record OculosUpdateCorDTO(
        @NotNull(message = "O ID da nova cor não pode ser nulo.")
        Integer idNovaCor
) {}