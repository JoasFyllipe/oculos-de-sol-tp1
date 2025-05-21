package io.github.JoasFyllipe.service.marca;

import java.util.List;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;

public interface MarcaService{

    MarcaResponseDTO create(MarcaRequestDTO marcaDTO);
    void update(Long id, MarcaRequestDTO marcaDTO);
    void delete(Long id);
    MarcaResponseDTO findById(Long id);
    MarcaResponseDTO findByNome(String nome);
    List<MarcaResponseDTO> findAll();
}
