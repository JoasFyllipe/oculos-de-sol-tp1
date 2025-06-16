package io.github.JoasFyllipe.dto.pagamento;

import io.github.JoasFyllipe.model.pagamento.Pix;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PixResponseDTO(
        String chave,
        LocalDateTime validade,
        BigDecimal valor
) {

    public static PixResponseDTO valueOf(Pix pix) {
        return new PixResponseDTO(
                pix.getChave(),
                pix.getValidade(),
                pix.getValor());
    }
}