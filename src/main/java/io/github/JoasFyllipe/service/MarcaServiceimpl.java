package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.MarcaDTO;
import io.github.JoasFyllipe.dto.MarcaResponseDTO;
import io.github.JoasFyllipe.model.Marca;
import io.github.JoasFyllipe.model.Oculos;
import io.github.JoasFyllipe.repository.MarcaRepository;
import io.github.JoasFyllipe.repository.OculosRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class MarcaServiceimpl implements MarcaService{

    @Inject
    MarcaRepository marcaRepository;

    @Inject
    OculosRepository oculosRepository;

    @Override
    @Transactional
    public MarcaResponseDTO create(MarcaDTO marcaDTO){
        Marca novaMarca = new Marca();
        novaMarca.setName(marcaDTO.nome());

        Oculos oculos = oculosRepository.findById(marcaDTO.idOculos());

    }

    @Override
    public void update(Long id, MarcaDTO marcaDTO) {

    }

    @Override
    public MarcaResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<MarcaResponseDTO> findByNome(String nome) {
        return List.of();
    }

    @Override
    public List<MarcaResponseDTO> findAll() {
        return List.of();
    }
}
