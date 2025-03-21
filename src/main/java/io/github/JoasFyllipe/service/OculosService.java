package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.dto.OculosResponseDTO;

import java.util.List;

public interface OculosService {

    OculosResponseDTO create(OculosDTO oculos);
    List<OculosResponseDTO> findAll();
    void update(Long id, OculosDTO oculosDTO);
    void delete(Long id);
    List<OculosResponseDTO> findByCor(String corOuId);
    List<OculosResponseDTO> findByGenero(String generoOuId);
    List<OculosResponseDTO> findByModelo(String modeloOuId);
}
