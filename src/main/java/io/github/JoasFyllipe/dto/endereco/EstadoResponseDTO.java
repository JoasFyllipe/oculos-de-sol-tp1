package io.github.JoasFyllipe.dto.endereco;

import io.github.JoasFyllipe.model.usuario.endereco.Estado;

public record EstadoResponseDTO(
        Long id,
        String nome,
        String sigla
) {
    public static EstadoResponseDTO valueOf(Estado estado) {
        return new EstadoResponseDTO(
                estado.getId(),
                estado.getNome(),
                estado.getSigla()
        );
    }
}
