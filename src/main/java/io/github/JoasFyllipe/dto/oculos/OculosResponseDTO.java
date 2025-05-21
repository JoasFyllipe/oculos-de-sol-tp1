package io.github.JoasFyllipe.dto.oculos;

import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.model.enums.CorArmacao;
import io.github.JoasFyllipe.model.enums.Genero;
import io.github.JoasFyllipe.model.enums.Modelo;
import io.github.JoasFyllipe.model.oculos.Oculos;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OculosResponseDTO(
        Long id,

        @NotBlank(message = "Nome do óculos não pode estar em branco")
        @Size(max = 100, message = "O nome do óculos não pode exceder 100 caracteres")
        String nome,

        @NotNull(message = "Valor do óculos não pode ser nulo")
        @DecimalMin(value = "0.01", inclusive = true, message = "O valor do óculos deve ser maior que zero")
        Double valor,

        @NotNull(message = "Quantidade de estoque não pode ser nula")
        @Min(value = 0, message = "A quantidade de estoque não pode ser negativa")
        Integer quantidadeEstoque,

        @NotNull(message = "Cor da armação não pode ser nula")
        CorArmacao corArmacao,

        @NotNull(message = "Gênero não pode ser nulo")
        Genero genero,

        @NotNull(message = "Modelo não pode ser nulo")
        Modelo modelo,

        @NotNull(message = "Marca não pode ser nula")
        MarcaResponseDTO marca) {

    public static OculosResponseDTO valueOf(Oculos oculos) {
        if (oculos == null) {
            return null;
        }
        return new OculosResponseDTO(
                oculos.getId(),
                oculos.getNome(),
                oculos.getValor(),
                oculos.getQuantidadeEstoque(),
                oculos.getCorArmacao(),
                oculos.getGenero(),
                oculos.getModelo(),
                MarcaResponseDTO.valueOf(oculos.getMarca())
        );
    }
}
