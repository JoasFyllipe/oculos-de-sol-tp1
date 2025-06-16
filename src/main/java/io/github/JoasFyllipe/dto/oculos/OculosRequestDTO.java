package io.github.JoasFyllipe.dto.oculos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record OculosRequestDTO(
        @NotBlank(message = "Nome não pode estar em branco")
        String nome,

        @NotNull(message = "Preço não pode ser nulo")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotNull(message = "Quantidade em estoque não pode ser nula")
        @Min(value = 0, message = "O estoque não pode ser negativo")
        Integer quantidadeEstoque,

        @NotNull(message = "O ID da cor da armação não pode ser nulo")
        Integer idCorArmacao,

        @NotNull(message = "O ID do gênero não pode ser nulo")
        Integer idGenero,

        @NotNull(message = "O ID do modelo não pode ser nulo")
        Integer idModelo,

        @NotNull(message = "O ID da marca não pode ser nulo")
        Long idMarca
) {}
