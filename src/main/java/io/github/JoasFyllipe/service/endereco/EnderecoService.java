package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import java.util.List;

public interface EnderecoService {

    EnderecoResponseDTO create(String email, EnderecoRequestDTO enderecoDTO);

    List<EnderecoResponseDTO> findByUsuario(String email);

    EnderecoResponseDTO findById(String email, Long enderecoId);

    void update(String email, Long enderecoId, EnderecoRequestDTO enderecoDTO);

    void delete(String email, Long enderecoId);
}