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

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO usuarioDTO) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioDTO.nome());
        System.out.println(usuarioDTO.cpf());
        novoUsuario.setCpf(usuarioDTO.cpf());
        novoUsuario.setTelefone(usuarioDTO.telefone());
        novoUsuario.setEmail(usuarioDTO.email());
        novoUsuario.setDataNascimento(usuarioDTO.dataNascimento());

        usuarioRepository.persist(novoUsuario);

        return new UsuarioResponseDTO(
            novoUsuario.getId(),
            novoUsuario.getNome(),
            novoUsuario.getCpf(),
            novoUsuario.getTelefone(),
            novoUsuario.getEmail(),
            novoUsuario.getDataNascimento()
        );
    }

    @Override
    @Transactional
    public void update(Long id, UsuarioUpdateRequestDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente != null) {
            usuarioExistente.setNome(usuarioDTO.nome());
            usuarioExistente.setEmail(usuarioDTO.email());
        } else {
            throw new UsuarioNotFoundException("Usuário não encontrado para o Id: " + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.deleteById(id)) {
            throw new UsuarioNotFoundException("Usuário não encontrado para o Id: " + id);
        }
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado para o Id: " + id);
        }
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getTelefone(),
            usuario.getEmail(),
            usuario.getDataNascimento()
        );
    }

    @Override
    public UsuarioResponseDTO findByNome(String nome) {
        Usuario usuario = usuarioRepository.findByNome(nome);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado para o nome: " + nome);
        }
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getTelefone(),
            usuario.getEmail(),
            usuario.getDataNascimento()
        );
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
            .stream()
            .map(usuario -> new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getDataNascimento()
            ))
            .collect(Collectors.toList());
    }
}
