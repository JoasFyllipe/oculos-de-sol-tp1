package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.CidadeRequestDTO;
import io.github.JoasFyllipe.dto.endereco.CidadeResponseDTO;
import io.github.JoasFyllipe.model.usuario.endereco.Cidade;
import io.github.JoasFyllipe.model.usuario.endereco.Estado;
import io.github.JoasFyllipe.repository.CidadeRepository;
import io.github.JoasFyllipe.repository.EstadoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CidadeServiceImpl implements CidadeService {

    @Inject
    CidadeRepository repository;

    @Inject
    EstadoRepository estadoRepository;

    @Override
    @Transactional
    public CidadeResponseDTO create(CidadeRequestDTO dto) {
        Estado estado = estadoRepository.findByIdOptional(dto.estadoId())
                .orElseThrow(() -> new NotFoundException("Estado não encontrado."));

        Cidade cidade = new Cidade();
        cidade.setNome(dto.nome());
        cidade.setEstado(estado);

        repository.persist(cidade);
        return CidadeResponseDTO.valueOf(cidade);
    }

    @Override
    @Transactional
    public CidadeResponseDTO update(Long id, CidadeRequestDTO dto) {
        Cidade cidade = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Cidade não encontrada."));

        Estado estado = estadoRepository.findByIdOptional(dto.estadoId())
                .orElseThrow(() -> new NotFoundException("Estado não encontrado."));

        cidade.setNome(dto.nome());
        cidade.setEstado(estado);
        return CidadeResponseDTO.valueOf(cidade);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.deleteById(id)) {
            throw new NotFoundException("Cidade não encontrada.");
        }
    }

    @Override
    public CidadeResponseDTO findById(Long id) {
        return repository.findByIdOptional(id)
                .map(CidadeResponseDTO::valueOf)
                .orElseThrow(() -> new NotFoundException("Cidade não encontrada."));
    }

    @Override
    public List<CidadeResponseDTO> findAll() {
        return repository.listAll().stream()
                .map(CidadeResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public List<CidadeResponseDTO> findByEstado(Long estadoId) {
        return repository.findByEstadoId(estadoId).stream()
                .map(CidadeResponseDTO::valueOf)
                .collect(Collectors.toList());
    }
}