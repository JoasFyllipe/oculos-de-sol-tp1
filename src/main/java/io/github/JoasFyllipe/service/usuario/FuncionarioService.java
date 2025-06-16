package io.github.JoasFyllipe.service.usuario;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import java.util.List;

public interface FuncionarioService {
    // Métodos para ADMIN
    FuncionarioResponseDTO create(FuncionarioRequestDTO funcionarioDTO);
    FuncionarioResponseDTO update(Long id, FuncionarioUpdateRequestDTO funcionarioDTO);
    void delete(Long id);
    FuncionarioResponseDTO findById(Long id);
    List<FuncionarioResponseDTO> findAll();
    List<FuncionarioResponseDTO> findByCargo(String cargo);
    FuncionarioResponseDTO findByNome(String nome);
    FuncionarioResponseDTO findByEmail(String email);

    // Métodos de autoatendimento para o próprio funcionário (usando email)
    void updateEndereco (String email, List<EnderecoRequestDTO>endereco);
    void updateTelefone (String email, List<TelefoneRequestDTO>telefone);
    void updateTelefoneEspecifico(String email, Long idTelefone, TelefoneRequestDTO telefoneDTO);
    void updateSenha(String email, SenhaPatchRequestDTO senhaPatch);
    void updateCpf(String email, CpfPatchRequestDTO cpfPatch);
    void updateNome(String email, NomePatchRequestDTO nomePatch);
    void updateEmail(String email, EmailPatchRequestDTO emailPatch);
    void updateDataNascimento(String email, DataNascimentoPatchDTO dataNascimentoPatch);
    void updateSalario(String email, SalarioPatchRequestDTO salarioDTO);
}