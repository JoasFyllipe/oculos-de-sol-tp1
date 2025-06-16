package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.exceptions.EnderecoNotFoundException;
import io.github.JoasFyllipe.exceptions.UsuarioNotFoundException;
import io.github.JoasFyllipe.model.usuario.endereco.Cidade;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.repository.CidadeRepository;
import io.github.JoasFyllipe.repository.EnderecoRepository;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public EnderecoResponseDTO create(String email, EnderecoRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário com email " + email + " não encontrado.");
        }

        Endereco endereco = mapToEntity(new Endereco(), dto);
        endereco.setUsuario(usuario); // Associa o endereço ao usuário correto

        enderecoRepository.persist(endereco);
        return EnderecoResponseDTO.valueOf(endereco);
    }

    @Override
    public List<EnderecoResponseDTO> findByUsuario(String email) {
        return enderecoRepository.findByUsuarioEmail(email).stream()
                .map(EnderecoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public EnderecoResponseDTO findById(String email, Long enderecoId) {
        Endereco endereco = enderecoRepository.findByUsuarioEmailAndId(email, enderecoId);
        if (endereco == null) {
            throw new EnderecoNotFoundException("Endereço com ID " + enderecoId + " não encontrado para este usuário.");
        }
        return EnderecoResponseDTO.valueOf(endereco);
    }

    @Override
    @Transactional
    public void update(String email, Long enderecoId, EnderecoRequestDTO dto) {
        Endereco endereco = enderecoRepository.findByUsuarioEmailAndId(email, enderecoId);
        if (endereco == null) {
            throw new EnderecoNotFoundException("Endereço com ID " + enderecoId + " não encontrado para este usuário.");
        }
        mapToEntity(endereco, dto); // Reutiliza o método para atualizar os campos
    }

    @Override
    @Transactional
    public void delete(String email, Long enderecoId) {
        Endereco endereco = enderecoRepository.findByUsuarioEmailAndId(email, enderecoId);
        if (endereco == null) {
            throw new EnderecoNotFoundException("Endereço com ID " + enderecoId + " não encontrado para este usuário.");
        }
        enderecoRepository.delete(endereco);
    }

    private Endereco mapToEntity(Endereco endereco, EnderecoRequestDTO dto) {
        Cidade cidade = cidadeRepository.findById(dto.idCidade());
        if (cidade == null) {
            throw new ValidationException("Cidade com ID " + dto.idCidade() + " não encontrada.");
        }

        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCep(dto.cep());
        endereco.setCidade(cidade);

        return endereco;
    }
}