package io.github.JoasFyllipe.service.marca;

import java.util.List;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.exceptions.MarcaNotFoundException;
import io.github.JoasFyllipe.model.marca.Marca;
import io.github.JoasFyllipe.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MarcaServIceimpl implements MarcaService {

    @Inject
    MarcaRepository marcaRepository;

    @Override
    @Transactional
    public MarcaResponseDTO create(MarcaRequestDTO marcaDTO) {
        Marca novaMarca = new Marca(); // Ou marcaDTO.toEntity() se você preferir o método no DTO
        novaMarca.setNome(marcaDTO.nome());

        marcaRepository.persist(novaMarca); // Persiste a nova entidade Marca

        return new MarcaResponseDTO(novaMarca.getId(), novaMarca.getNome());
    }

    @Override
    @Transactional
    public void update(Long id, MarcaRequestDTO marcaDTO) {
        Marca edicaoMarca = marcaRepository.findById(id);
        if (edicaoMarca != null) {
            edicaoMarca.setNome(marcaDTO.nome());
        } else {
            throw new MarcaNotFoundException("Marca não encontrada para o Id: " + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // PanacheRepository.deleteById retorna boolean: true se deletado, false se não encontrado
        if (!marcaRepository.deleteById(id)) {
            throw new MarcaNotFoundException("Marca não encontrada para o Id: " + id);
        }
    }

    @Override
    public MarcaResponseDTO findById(Long id) {
        Marca marca = marcaRepository.findById(id);
        if (marca == null) {
            throw new MarcaNotFoundException("Marca não encontrada para o Id: " + id);
        }
        return MarcaResponseDTO.valueOf(marca);
    }

    @Override
    public MarcaResponseDTO findByNome(String nome) {
        // CORREÇÃO: Usar o método findByNome do repositório para eficiência
        Marca marca = marcaRepository.findByNome(nome);
        if (marca == null) {
            throw new MarcaNotFoundException("Marca não encontrada para o nome: " + nome);
        }
        return MarcaResponseDTO.valueOf(marca);
    }

    @Override
    public List<MarcaResponseDTO> findAll() {
        // PanacheRepository.findAll() retorna uma PanacheQuery, use .list() para obter a lista
        return marcaRepository.findAll()
                .list() // Usar .list() para obter a lista de entidades
                .stream()
                .map(MarcaResponseDTO::valueOf)
                .toList();
    }
}