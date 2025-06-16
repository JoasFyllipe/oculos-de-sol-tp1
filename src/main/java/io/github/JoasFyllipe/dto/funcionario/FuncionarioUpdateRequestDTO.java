package io.github.JoasFyllipe.dto.funcionario;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal; // Import para Salário
import java.time.LocalDate;
import java.util.List;

public record FuncionarioUpdateRequestDTO(
        @NotBlank(message = "Campo nome não deve ser vazio")
        String nome,

        @NotBlank(message = "Campo cpf não deve ser vazio")
        String cpf,

        @NotBlank(message = "Campo data de nascimento não deve ser vazio")
        LocalDate dataNascimento,

        // CORRIGIDO: Campos do Funcionário adicionados
        @NotBlank(message = "Campo cargo não deve ser vazio")
        String cargo,

        @NotNull @Positive
        BigDecimal salario,

        List<TelefoneRequestDTO> telefones,

        List<EnderecoRequestDTO> enderecos
) {}