package io.github.JoasFyllipe.dto.endereco;

import io.github.JoasFyllipe.model.usuario.endereco.Cidade;

public record CidadeResponseDTO(
        Long id,
        String nome,
        Long estadoId,
        String estadoNome,
        String estadoSigla
) {
    public static CidadeResponseDTO valueOf(Cidade cidade) {
        return new CidadeResponseDTO(
                cidade.getId(),
                cidade.getNome(),
                cidade.getEstado().getId(),
                cidade.getEstado().getNome(),
                cidade.getEstado().getSigla()
        );
    }
}