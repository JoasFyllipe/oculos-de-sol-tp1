package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.CidadeRequestDTO;
import io.github.JoasFyllipe.dto.endereco.CidadeResponseDTO;

import java.util.List;

public interface CidadeService {
    CidadeResponseDTO create(CidadeRequestDTO dto);
    CidadeResponseDTO update(Long id, CidadeRequestDTO dto);
    void delete(Long id);
    CidadeResponseDTO findById(Long id);
    List<CidadeResponseDTO> findAll();
    List<CidadeResponseDTO> findByEstado(Long estadoId);
}