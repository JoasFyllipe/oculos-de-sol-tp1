package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.EstadoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EstadoResponseDTO;
import io.github.JoasFyllipe.model.usuario.endereco.Estado;
import io.github.JoasFyllipe.repository.EstadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EstadoServiceImpl implements EstadoService {

    @Inject
    EstadoRepository repository;

    @Override
    @Transactional
    public EstadoResponseDTO create(EstadoRequestDTO dto) {
        if (repository.findBySigla(dto.sigla()) != null) {
            throw new ValidationException("A sigla informada já existe.");
        }

        Estado estado = new Estado();
        estado.setNome(dto.nome());
        estado.setSigla(dto.sigla());

        repository.persist(estado);
        return EstadoResponseDTO.valueOf(estado);
    }

    @Override
    @Transactional
    public EstadoResponseDTO update(Long id, EstadoRequestDTO dto) {
        Estado estado = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Estado não encontrado."));

        estado.setNome(dto.nome());
        estado.setSigla(dto.sigla());
        return EstadoResponseDTO.valueOf(estado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Estado não encontrado.");
        }
    }

    @Override
    public EstadoResponseDTO findById(Long id) {
        return repository.findByIdOptional(id)
                .map(EstadoResponseDTO::valueOf)
                .orElseThrow(() -> new NotFoundException("Estado não encontrado."));
    }

    @Override
    public List<EstadoResponseDTO> findAll() {
        return repository.findAllOrderedByName().stream()
                .map(EstadoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public EstadoResponseDTO findBySigla(String sigla) {
        Estado estado = repository.findBySigla(sigla);
        if (estado == null) {
            throw new NotFoundException("Estado não encontrado para a sigla: " + sigla);
        }
        return EstadoResponseDTO.valueOf(estado);
    }

    @Override
    public List<EstadoResponseDTO> findByNomeLike(String nome) {
        return repository.findByNomeLike(nome).stream()
                .map(EstadoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }
}