package io.github.JoasFyllipe.dto.funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.github.JoasFyllipe.dto.usuario.UsuarioRequestDTO;
import io.github.JoasFyllipe.model.funcionario.Funcionario;
import io.github.JoasFyllipe.model.usuario.Usuario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FuncionarioRequestDTO(
    @NotNull(message = "Usuário é obrigatório")
    long usuario,

    @NotNull(message = "Cargo é obrigatório")
    @Size(min = 2, max = 50, message = "O cargo deve ter entre 2 e 50 caracteres")
    String cargo,

    @NotNull(message = "Data de contratação é obrigatória")
    @PastOrPresent(message = "A data de contratação não pode ser no futuro")
    LocalDate dataContratacao,

    @NotNull(message = "Salário é obrigatório")
    String salario) {

    public static Funcionario toEntity(FuncionarioRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Funcionario funcionario = new Funcionario();
        funcionario.setCargo(dto.cargo());
        funcionario.setDataContratacao(dto.dataContratacao());
        funcionario.setSalario(dto.salario());
        return funcionario;
    }
}
