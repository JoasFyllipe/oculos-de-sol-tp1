package io.github.JoasFyllipe.dto.oculos;

import io.github.JoasFyllipe.dto.marca.MarcaResponseDTO;
import io.github.JoasFyllipe.model.oculos.Oculos;
import java.math.BigDecimal;

public record OculosResponseDTO(
        Long id,
        String nome,
        BigDecimal preco,
        Integer quantidadeEstoque,
        String corArmacao,
        String genero,
        String modelo,
        MarcaResponseDTO marca
) {
    public static OculosResponseDTO valueOf(Oculos oculos) {
        return new OculosResponseDTO(
                oculos.getId(),
                oculos.getNome(),
                oculos.getPreco(),
                oculos.getQuantidadeEstoque(),
                oculos.getCorArmacao().getLabel(),
                oculos.getGenero().getLabel(),
                oculos.getModelo().getLabel(),
                MarcaResponseDTO.valueOf(oculos.getMarca())
        );
    }
}
