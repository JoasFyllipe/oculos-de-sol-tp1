package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.MarcaDTO;
import io.github.JoasFyllipe.dto.MarcaResponseDTO;

import java.util.List;

public interface MarcaService{

    MarcaResponseDTO create(MarcaDTO marcaDTO);
    void update(Long id, MarcaDTO marcaDTO);
    void delete(Long id);
    MarcaResponseDTO findById(Long id);
    MarcaResponseDTO findByNome(String nome);
    List<MarcaResponseDTO> findAll();
}
