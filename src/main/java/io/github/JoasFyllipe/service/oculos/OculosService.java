package io.github.JoasFyllipe.service.oculos;

import io.github.JoasFyllipe.dto.oculos.*;
import io.github.JoasFyllipe.dto.oculos.patch.*;

import java.util.List;

public interface OculosService {

    OculosResponseDTO create(OculosRequestDTO dto);
    OculosResponseDTO update(Long id, OculosRequestDTO dto);
    void delete(Long id);

    void updateEstoque(Long id, OculosUpdateEstoqueDTO dto);
    void updateNome(Long id, OculosUpdateNomeDTO dto);
    void updatePreco(Long id, OculosUpdatePrecoDTO dto);
    void updateCor(Long id, OculosUpdateCorDTO dto);
    void updateModelo(Long id, OculosUpdateModeloDTO dto);

    OculosResponseDTO findById(Long id);
    List<OculosResponseDTO> findAll();
    List<OculosResponseDTO> findByCor(Integer idCor);
    List<OculosResponseDTO> findByGenero(Integer idGenero);
    List<OculosResponseDTO> findByModelo(Integer idModelo);
    List<OculosResponseDTO> findByMarca(Long idMarca);
}