package io.github.JoasFyllipe.dto.telefone;

import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import io.github.JoasFyllipe.model.usuario.telefone.TipoTelefone;

public record TelefoneResponseDTO(
        Long id,
        String ddd,
        String numero,
        Boolean principal,
        TipoTelefone tipo
) {

    public static TelefoneResponseDTO valueOf(Telefone telefone) {
        if (telefone == null) {
            return null;
        }
        return new TelefoneResponseDTO(
                telefone.getId(),
                telefone.getDdd(),
                telefone.getNumero(),
                telefone.isPrincipal(),
                telefone.getTipo()
        );
    }
}