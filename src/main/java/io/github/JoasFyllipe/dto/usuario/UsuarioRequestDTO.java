package io.github.JoasFyllipe.dto.usuario;

import java.time.LocalDate;
import java.util.List;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.model.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100)
    String nome,

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos")
    String cpf,

    @NotNull(message = "Telefone é obrigatório")
    @Size(max = 15)
    List<TelefoneRequestDTO> telefones,

    @NotNull(message = "Telefone é obrigatório")
    @Size(max = 15)
    List<EnderecoRequestDTO> enderecos,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100)
    String email,

    @NotBlank(message = "Senha é obrigatório")
    String senha,

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve estar no passado")
    LocalDate dataNascimento) {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setTelefone(dto.telefones()
                .stream()
                .map(TelefoneRequestDTO::toEntity)
                .toList()
        );
        return usuario;
    }
}
