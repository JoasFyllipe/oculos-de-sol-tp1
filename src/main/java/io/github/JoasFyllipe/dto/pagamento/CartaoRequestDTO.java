package io.github.JoasFyllipe.dto.pagamento;

import io.github.JoasFyllipe.model.pagamento.ModalidadeCartao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CartaoRequestDTO(
        @NotBlank(message = "O nome do titular deve ser informado.")
        String titular,

        @NotBlank(message = "O CPF do titular do cartão deve ser informado.")
        @Pattern(regexp = "^[0-9]{11}$", message = "O CPF deve conter 11 dígitos.")
        String cpfCartao,

        @NotBlank(message = "O número do cartão deve ser informado.")
        @Pattern(regexp = "^[0-9]{13,16}$", message = "Número de cartão inválido.")
        String numero,

        @NotBlank(message = "A data de validade deve ser informada.")
        @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "A data de validade deve estar no formato MM/YY.")
        String dataValidade,

        @NotBlank(message = "O CVC deve ser informado.")
        @Size(min = 3, max = 4, message = "O código CVC deve possuir de 3 a 4 caracteres.")
        String cvc,

        @NotNull(message = "O ID da modalidade do cartão deve ser informado.")
        Integer idModalidadeCartao
) {}