package io.github.JoasFyllipe.dto.usuario;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

// Removed duplicate empty class declaration
public record UsuarioUpdateRequestDTO(
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,

    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "CPF não pode estar em branco")
    @Size(max = 11, message = "CPF deve ter no máximo 11 caracteres, apenas numeros")
    String cpf,

    @Past(message = "Data de nascimento deve ser uma data no passado")
    LocalDate dataNascimento
    
    ) {
}