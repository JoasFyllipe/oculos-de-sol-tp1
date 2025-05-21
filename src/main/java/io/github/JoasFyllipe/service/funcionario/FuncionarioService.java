package io.github.JoasFyllipe.service.funcionario;

import java.util.List;

import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;

public interface FuncionarioService {

    FuncionarioResponseDTO create(FuncionarioRequestDTO funcionarioDTO);

    void update(Long id, FuncionarioUpdateRequestDTO funcionarioDTO);

    void delete(Long id);

    FuncionarioResponseDTO findById(Long id);

    FuncionarioResponseDTO findByNome(String nome);

    List<FuncionarioResponseDTO> findAll();
}
