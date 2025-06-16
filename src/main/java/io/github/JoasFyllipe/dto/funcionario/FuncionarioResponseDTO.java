package io.github.JoasFyllipe.dto.funcionario;

import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.model.usuario.Funcionario;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record FuncionarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento, // CORRIGIDO: Adicionado para uma resposta mais completa.
        String email,
        List<TelefoneResponseDTO> telefones,
        List<EnderecoResponseDTO> enderecos, // CORRIGIDO: Nome do campo corrigido de 'endereros'.
        String cargo,
        LocalDate dataContratacao
) {
    public static FuncionarioResponseDTO valueOf(Funcionario funcionario) {
        if (funcionario == null || funcionario.getUsuario() == null) {
            return null;
        }

        var usuario = funcionario.getUsuario();

        // CORRIGIDO: A lógica de mapeamento foi ajustada para o novo formato do record.
        return new FuncionarioResponseDTO(
                funcionario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getDataNascimento(),
                usuario.getEmail(),
                usuario.getTelefone() != null ?
                        usuario.getTelefone()
                                .stream()
                                .map(TelefoneResponseDTO::valueOf)
                                .collect(Collectors.toList()) : Collections.emptyList(),
                usuario.getEndereco() != null ?
                        usuario.getEndereco()
                                .stream()
                                .map(EnderecoResponseDTO::valueOf)
                                .collect(Collectors.toList()) : Collections.emptyList(),
                funcionario.getCargo(),
                funcionario.getDataContratacao()
        );
    }
}