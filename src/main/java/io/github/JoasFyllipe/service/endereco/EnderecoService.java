package io.github.JoasFyllipe.service.endereco;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;

import java.util.List;

public interface EnderecoService {

    EnderecoResponseDTO create(EnderecoRequestDTO enderecoDTO);

    List<EnderecoResponseDTO> findAll();

    EnderecoResponseDTO findById(Long id);

    void update(Long id, EnderecoRequestDTO enderecoDTO);

    void delete(Long id);
}
