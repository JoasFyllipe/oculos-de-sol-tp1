package io.github.JoasFyllipe.dto.oculos;

import io.github.JoasFyllipe.dto.marca.MarcaRequestDTO;
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

public record OculosRequestDTO(

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(max = 100, message = "O nome do óculos não pode exceder 100 caracteres")
    String nome,

    @NotNull(message = "Valor não pode ser nulo")
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

    // Método para converter o DTO em uma entidade Oculos
    public static Oculos toEntity(OculosRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Oculos(
                    dto.nome(),
                    dto.valor(),
                    dto.quantidadeEstoque(),
                    dto.corArmacao(),
                    dto.genero(),
                    dto.modelo(),
                    MarcaRequestDTO.toEntity(dto.marca())
                );
    }
}
