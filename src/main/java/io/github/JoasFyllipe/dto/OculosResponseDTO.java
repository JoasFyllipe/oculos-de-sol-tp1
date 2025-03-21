package io.github.JoasFyllipe.dto;

import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Genero;
import io.github.JoasFyllipe.model.Modelo;
import io.github.JoasFyllipe.model.Oculos;

public record OculosResponseDTO(
        Long id,
        String nome,
        Double valor,
        Integer quantidadeEstoque,
        CorArmacao corArmacao,
        Genero genero,
        Modelo modelo) {


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
                oculos.getModelo());
    }
}
