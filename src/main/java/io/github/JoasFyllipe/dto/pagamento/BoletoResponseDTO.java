package io.github.JoasFyllipe.dto.pagamento;

import io.github.JoasFyllipe.model.pagamento.Boleto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BoletoResponseDTO(
        String codigoBarras,
        LocalDateTime validade,
        BigDecimal valor
){

    public static BoletoResponseDTO valueOf(Boleto boleto) {
        return new BoletoResponseDTO(
                boleto.getCodigoBarras(),
                boleto.getValidade(),
                boleto.getValor());
    }

}