package io.github.JoasFyllipe.dto.marca;

import io.github.JoasFyllipe.model.marca.Marca;

public record MarcaResponseDTO(
        Long id,
        String nome
) {
        public static MarcaResponseDTO valueOf(Marca marca) {
                if (marca == null) {
                        return null;
                }
                return new MarcaResponseDTO(marca.getId(), marca.getNome());
        }
}