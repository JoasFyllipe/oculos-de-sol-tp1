package io.github.JoasFyllipe.service.usuario;

import java.util.List;
import java.util.stream.Collectors;
import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.exceptions.UsuarioNotFoundException;
import io.github.JoasFyllipe.dto.usuario.UsuarioUpdateRequestDTO;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;


    @Override
    public UsuarioResponseDTO findByEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email);
        if(usuario == null)
            throw new ValidationException("Email não encontrado");

        return UsuarioResponseDTO.valueOf(usuario);

    }
    @Override
    public Usuario findByUsernameAndSenha(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha);
    }

    @Override
    public UsuarioResponseDTO findByUsername(String username) {
        var usuario = usuarioRepository.findByUsername(username);
        if(usuario == null)
            throw new ValidationException("Username não encontrado");

        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public UsuarioResponseDTO findByCpf(String cpf) {
        var usuario = usuarioRepository.findByCpf(cpf);
        if(usuario == null)
            throw new ValidationException("cpf não encontrado");

        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll().list()
                .stream()
                .map(UsuarioResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if(usuario == null)
            throw new UsuarioNotFoundException("Usuario não encontrado. Id: "+ id);
        return UsuarioResponseDTO.valueOf(usuario);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}