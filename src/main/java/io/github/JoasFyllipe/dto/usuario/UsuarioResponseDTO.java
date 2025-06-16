package io.github.JoasFyllipe.dto.usuario;

import java.time.LocalDate;
import java.util.List;

import io.github.JoasFyllipe.dto.endereco.EnderecoResponseDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneResponseDTO;
import io.github.JoasFyllipe.model.usuario.Perfil;
import io.github.JoasFyllipe.model.usuario.Usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        List<Perfil> perfil,
        List<EnderecoResponseDTO> endereco,
        List<TelefoneResponseDTO> telefones,
        LocalDate dataNascimento) {

    public static UsuarioResponseDTO valueOf(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getEmail(),
            usuario.getPerfis(),
            usuario.getEndereco()
                    .stream()
                    .map(EnderecoResponseDTO::valueOf)
                    .toList(),
            usuario.getTelefone()
                    .stream()
                    .map(TelefoneResponseDTO::valueOf)
                    .toList(),
            usuario.getDataNascimento()
        );
    }
}
