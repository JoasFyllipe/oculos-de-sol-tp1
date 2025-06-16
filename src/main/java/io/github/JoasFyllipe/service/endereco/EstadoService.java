package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.EstadoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EstadoResponseDTO;
import java.util.List;

public interface EstadoService {
    EstadoResponseDTO create(EstadoRequestDTO dto);
    EstadoResponseDTO update(Long id, EstadoRequestDTO dto);
    void delete(Long id);
    EstadoResponseDTO findById(Long id);
    List<EstadoResponseDTO> findAll();
    EstadoResponseDTO findBySigla(String sigla);
    List<EstadoResponseDTO> findByNomeLike(String nome);
}