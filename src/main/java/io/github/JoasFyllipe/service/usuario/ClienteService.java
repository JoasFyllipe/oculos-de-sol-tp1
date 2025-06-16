package io.github.JoasFyllipe.service.usuario;

import io.github.JoasFyllipe.dto.cliente.ClienteRequestDTO;
import io.github.JoasFyllipe.dto.cliente.ClienteResponseDTO;
import io.github.JoasFyllipe.dto.cliente.ClienteUpdateRequestDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.model.usuario.Cliente;
import java.util.List;

public interface ClienteService {
    ClienteResponseDTO create(ClienteRequestDTO clienteDTO);
    List<ClienteResponseDTO> findAll();
    ClienteResponseDTO findById(Long id);
    ClienteResponseDTO findByEmail(String email);
    List<ClienteResponseDTO> findByNome (String nome);
    ClienteResponseDTO clienteFromFuncionario(String email);

    ClienteResponseDTO findByUsuario(String email);

    ClienteResponseDTO getMinhasInformacoes(String email);
    Cliente update(String email, ClienteUpdateRequestDTO clienteDTO);

    void delete(Long id);
    void updateEndereco (String email, List<EnderecoRequestDTO>endereco);
    void updateTelefone (String email, List<TelefoneRequestDTO>telefone);
    void updateTelefoneEspecifico(String email, Long idTelefone, TelefoneRequestDTO telefoneDTO);

    // MÉTODOS PATCH
    void updateSenha(String email, SenhaPatchRequestDTO senhaPatch);
    void updateCpf(String email, CpfPatchRequestDTO cpfPatch);
    void updateNome(String email, NomePatchRequestDTO nomePatch);
    void updateEmail(String email, EmailPatchRequestDTO emailPatch);
    void updateDataNascimento(String email, DataNascimentoPatchDTO dataNascimentoPatch);
}