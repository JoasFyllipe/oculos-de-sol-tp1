package io.github.JoasFyllipe.service.oculos;

import io.github.JoasFyllipe.dto.oculos.OculosRequestDTO;
import io.github.JoasFyllipe.dto.oculos.OculosResponseDTO;

import java.util.List;

public interface OculosService {

    OculosResponseDTO create(OculosRequestDTO oculos);
    List<OculosResponseDTO> findAll();
    void update(Long id, OculosRequestDTO oculosDTO);
    void delete(Long id);
    List<OculosResponseDTO> findByCor(String corOuId);
    List<OculosResponseDTO> findByGenero(String generoOuId);
    List<OculosResponseDTO> findByModelo(String modeloOuId);
    List<OculosResponseDTO> findByMarca(String marcaOuId);
    OculosResponseDTO findById(Long id);
}
