package io.github.JoasFyllipe.dto.oculos.patch;

import jakarta.validation.constraints.NotNull;

public record OculosUpdateModeloDTO(
        @NotNull(message = "O ID do novo modelo não pode ser nulo.")
        Integer idNovoModelo
) {}