package io.github.JoasFyllipe.service;

import java.util.List;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.exceptions.MarcaNotFoundException;
import io.github.JoasFyllipe.model.Marca;
import io.github.JoasFyllipe.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MarcaServiceimpl implements MarcaService {

    @Inject
    MarcaRepository marcaRepository;

    @Override
    @Transactional
    public MarcaResponseDTO create(MarcaRequestDTO marcaDTO) {
        Marca novaMarca = new Marca();
        novaMarca.setNome(marcaDTO.getNome());

        marcaRepository.persist(novaMarca);

        return new MarcaResponseDTO(novaMarca.getId(), novaMarca.getNome()); 
    }

    @Override
    @Transactional
    public void update(Long id, MarcaRequestDTO marcaDTO) {
        Marca edicaoMarca = marcaRepository.findById(id);
        if (edicaoMarca != null) {
            edicaoMarca.setNome(marcaDTO.getNome());
        } else {
            throw new MarcaNotFoundException("Marca não encontrada para o Id: " + id);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
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
        Marca marca = Marca.fromNome(nome, marcaRepository.listAll());
        if (marca == null) {
            throw new MarcaNotFoundException("Marca não encontrada para o nome: " + nome);
        }
        return MarcaResponseDTO.valueOf(marca);
    }

    @Override
    public List<MarcaResponseDTO> findAll() {
        return marcaRepository.findAll()
                .stream()
                .map(MarcaResponseDTO::valueOf)
                .toList();
    }
}
