package io.github.JoasFyllipe.service.token;

import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;

public interface JwtService {

    String generateJwt(UsuarioResponseDTO usuarioDTO);
}
