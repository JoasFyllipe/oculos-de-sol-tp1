package io.github.JoasFyllipe.service.telefone;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.exceptions.TelefoneNotFoundException;
import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import io.github.JoasFyllipe.model.usuario.telefone.TipoTelefone;
import io.github.JoasFyllipe.model.usuario.Usuario; // NOVO: Dependência do modelo de usuário
import io.github.JoasFyllipe.repository.TelefoneRepository;
import io.github.JoasFyllipe.repository.UsuarioRepository; // NOVO: Repositório de usuário para buscar o dono do telefone
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TelefoneServiceImpl implements TelefoneService {

    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    UsuarioRepository usuarioRepository; // NOVO

    @Override
    @Transactional
    public TelefoneResponseDTO create(String email, TelefoneRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }

        Telefone telefone = TelefoneRequestDTO.toEntity(dto);
        TipoTelefone tipo = TipoTelefone.valueOf(dto.idTipoTelefone()); // Converte o ID recebido no DTO para o tipo Entidade
        telefone.setTipo(tipo);
        telefone.setUsuario(usuario);
        telefoneRepository.persist(telefone);
        return TelefoneResponseDTO.valueOf(telefone);
    }

    @Override
    public List<TelefoneResponseDTO> findByUsuario(String email) {
        return telefoneRepository.list("usuario.email", email)
                .stream()
                .map(TelefoneResponseDTO::valueOf)
                .toList();
    }

    @Override
    public TelefoneResponseDTO findById(String email, Long telefoneId) {
        Telefone telefone = telefoneRepository.findByUsuarioEmailAndId(email, telefoneId);
        if (telefone == null) {
            throw new TelefoneNotFoundException("Telefone com ID " + telefoneId + " não encontrado para este usuário.");
        }
        return TelefoneResponseDTO.valueOf(telefone);
    }

    @Override
    @Transactional
    public void update(String email, Long telefoneId, TelefoneRequestDTO dto) {
        Telefone telefone = telefoneRepository.findByUsuarioEmailAndId(email, telefoneId);
        if (telefone == null) {
            throw new TelefoneNotFoundException("Telefone com ID " + telefoneId + " não encontrado para este usuário.");
        }
        telefone.setNumero(dto.numero());
        telefone.setDdd(dto.ddd());
        telefone.setPrincipal(dto.principal());

        TipoTelefone tipo = TipoTelefone.valueOf(dto.idTipoTelefone());
        telefone.setTipo(tipo);
    }

    @Override
    @Transactional
    public void delete(String email, Long telefoneId) {
        Telefone telefone = telefoneRepository.findByUsuarioEmailAndId(email, telefoneId);
        if (telefone == null) {
            throw new TelefoneNotFoundException("Telefone com ID " + telefoneId + " não encontrado para este usuário.");
        }
        telefoneRepository.delete(telefone);
    }
}