package io.github.JoasFyllipe.service.usuario;

import java.util.List;

import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioUpdateRequestDTO;

public interface UsuarioService {

    UsuarioResponseDTO create(UsuarioRequestDTO usuarioDTO);
    void update(Long id, UsuarioUpdateRequestDTO usuarioDTO);
    void delete(Long id);
    UsuarioResponseDTO findById(Long id);
    UsuarioResponseDTO findByNome(String nome);
    List<UsuarioResponseDTO> findAll();
}
