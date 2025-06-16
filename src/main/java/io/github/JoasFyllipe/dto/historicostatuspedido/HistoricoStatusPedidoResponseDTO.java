package io.github.JoasFyllipe.dto.historicostatuspedido;

import io.github.JoasFyllipe.model.pedido.HistoricoStatusPedido;
import java.time.LocalDateTime;

public record HistoricoStatusPedidoResponseDTO(
        LocalDateTime dataHora,
        String situacao,
        String observacao
) {
    public static HistoricoStatusPedidoResponseDTO valueOf(HistoricoStatusPedido historico) {
        return new HistoricoStatusPedidoResponseDTO(
                historico.getDataHora(),
                historico.getSituacao().getDescricao(), // Usando a descrição do Enum
                historico.getObservacao()
        );
    }
}