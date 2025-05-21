package io.github.JoasFyllipe.dto.usuario;

import java.time.LocalDate;

import io.github.JoasFyllipe.model.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioResponseDTO(
    Long id,

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,

    @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 dígitos")
    String cpf,

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    String telefone,

    @Email(message = "Email inválido")
    String email,

    LocalDate dataNascimento) {

    public static UsuarioResponseDTO valueOf(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getTelefone(),
            usuario.getEmail(),
            usuario.getDataNascimento()
        );
    }

    public UsuarioResponseDTO() {
        this(null, null, null, null, null, null);
    }

    public UsuarioResponseDTO(Long id, String nome, String email) {
        this(id, nome, null, null, email, null);
    }

    public UsuarioResponseDTO(Long id, String nome, String cpf, String telefone, String email, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }
}
