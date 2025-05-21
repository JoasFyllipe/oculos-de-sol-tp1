package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.exceptions.EnderecoNotFoundException;
import io.github.JoasFyllipe.model.endereco.Endereco;
import io.github.JoasFyllipe.repository.EnderecoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {

    @Inject
    EnderecoRepository enderecoRepository;

    @Override
    @Transactional
    public EnderecoResponseDTO create(EnderecoRequestDTO dto) {
        Endereco endereco = EnderecoRequestDTO.toEntity(dto);
        enderecoRepository.persist(endereco);
        return EnderecoResponseDTO.valueOf(endereco);
    }

    @Override
    public List<EnderecoResponseDTO> findAll() {
        return enderecoRepository.findAll()
                .stream()
                .map(EnderecoResponseDTO::valueOf)
                .toList();
    }

    @Override
    public EnderecoResponseDTO findById(Long id) {
        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null) {
            throw new EnderecoNotFoundException("Endereço não encontrado com ID: " + id);
        }
        return EnderecoResponseDTO.valueOf(endereco);
    }

    @Override
    @Transactional
    public void update(Long id, EnderecoRequestDTO dto) {
        Endereco endereco = enderecoRepository.findById(id);
        if (endereco == null) {
            throw new EnderecoNotFoundException("Endereço não encontrado com ID: " + id);
        }
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!enderecoRepository.deleteById(id)) {
            throw new EnderecoNotFoundException("Endereço não encontrado com ID: " + id);
        }
    }
}
