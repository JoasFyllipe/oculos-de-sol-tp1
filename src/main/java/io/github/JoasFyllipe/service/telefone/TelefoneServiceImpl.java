package io.github.JoasFyllipe.service.telefone;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.exceptions.TelefoneNotFoundException;
import io.github.JoasFyllipe.model.telefone.Telefone;
import io.github.JoasFyllipe.repository.TelefoneRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TelefoneServiceImpl implements TelefoneService {

    @Inject
    TelefoneRepository telefoneRepository;

    @Override
    @Transactional
    public TelefoneResponseDTO create(TelefoneRequestDTO dto) {
        Telefone telefone = TelefoneRequestDTO.toEntity(dto);
        telefoneRepository.persist(telefone);
        return TelefoneResponseDTO.valueOf(telefone);
    }

    @Override
    public List<TelefoneResponseDTO> findAll() {
        return telefoneRepository.findAll()
                .stream()
                .map(TelefoneResponseDTO::valueOf)
                .toList();
    }

    @Override
    public TelefoneResponseDTO findById(Long id) {
        Telefone telefone = telefoneRepository.findById(id);
        if (telefone == null) {
            throw new TelefoneNotFoundException("Telefone não encontrado com ID: " + id);
        }
        return TelefoneResponseDTO.valueOf(telefone);
    }

    @Override
    @Transactional
    public void update(Long id, TelefoneRequestDTO dto) {
        Telefone telefone = telefoneRepository.findById(id);
        if (telefone == null) {
            throw new TelefoneNotFoundException("Telefone não encontrado com ID: " + id);
        }
        telefone.setNumero(dto.numero());
        telefone.setTipo(dto.tipo());
        telefone.setPrincipal(dto.principal());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!telefoneRepository.deleteById(id)) {
            throw new TelefoneNotFoundException("Telefone não encontrado com ID: " + id);
        }
    }
}
