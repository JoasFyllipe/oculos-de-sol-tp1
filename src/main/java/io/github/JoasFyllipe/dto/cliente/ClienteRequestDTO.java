package io.github.JoasFyllipe.dto.cliente;

import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import jakarta.validation.Valid;

public record ClienteRequestDTO(
        @Valid
        UsuarioRequestDTO usuario
) {}