package io.github.JoasFyllipe.service.telefone;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;

import java.util.List;

public interface TelefoneService {

    TelefoneResponseDTO create(TelefoneRequestDTO telefoneDTO);

    List<TelefoneResponseDTO> findAll();

    void update(Long id, TelefoneRequestDTO telefoneDTO);

    void delete(Long id);

    TelefoneResponseDTO findById(Long id);
}
