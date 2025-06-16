package io.github.JoasFyllipe.service.usuario;

import java.util.List;

import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.model.usuario.Usuario;

public interface UsuarioService {

    UsuarioResponseDTO findByEmail(String email);
    UsuarioResponseDTO findByUsername(String username);
    UsuarioResponseDTO findByCpf(String cpf);
    List<UsuarioResponseDTO> findAll();
    UsuarioResponseDTO findById(Long id);
    void delete(Long id);
    Usuario findByUsernameAndSenha(String email, String senha);
}
