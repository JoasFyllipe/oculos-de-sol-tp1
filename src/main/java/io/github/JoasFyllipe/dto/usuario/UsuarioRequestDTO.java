package io.github.JoasFyllipe.dto.usuario;

import java.time.LocalDate;

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

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 15)
    String telefone,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100)
    String email,

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve estar no passado")
    LocalDate dataNascimento) {

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setCpf(dto.cpf());
        usuario.setTelefone(dto.telefone());
        usuario.setEmail(dto.email());
        usuario.setDataNascimento(dto.dataNascimento());
        return usuario;
    }
}
