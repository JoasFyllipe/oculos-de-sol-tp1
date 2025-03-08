package io.github.JoasFyllipe.service;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.model.Oculos;

import java.util.List;

public interface OculosService {

    Oculos create(OculosDTO oculos);
    List<Oculos> findAll();
}
