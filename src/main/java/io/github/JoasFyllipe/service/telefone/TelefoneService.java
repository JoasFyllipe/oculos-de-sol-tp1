package io.github.JoasFyllipe.service.telefone;

import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import java.util.List;

public interface TelefoneService {
    TelefoneResponseDTO create(String email, TelefoneRequestDTO telefoneDTO);

    List<TelefoneResponseDTO> findByUsuario(String email);

    TelefoneResponseDTO findById(String email, Long telefoneId);

    void update(String email, Long telefoneId, TelefoneRequestDTO telefoneDTO);

    void delete(String email, Long telefoneId);
}