package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Oculos;

import java.util.List;

public interface OculosService {

    Oculos create(OculosDTO oculos);
    List<Oculos> findAll();
    void update(Long id, OculosDTO oculosDTO);
    void delete(Long id);
    List<OculosDTO> findByCor(String corOuId);
}
