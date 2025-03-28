package io.github.JoasFyllipe.dto;

import io.github.JoasFyllipe.model.*;

public record OculosResponseDTO(
        Long id,
        String nome,
        Double valor,
        Integer quantidadeEstoque,
        CorArmacao corArmacao,
        Genero genero,
        Modelo modelo,
        MarcaResponseDTO marca) {


    public static OculosResponseDTO valueOf(Oculos oculos){
        if(oculos == null){
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
