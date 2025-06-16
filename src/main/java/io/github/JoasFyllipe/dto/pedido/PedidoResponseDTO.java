package io.github.JoasFyllipe.dto.pedido;

import io.github.JoasFyllipe.dto.historicostatuspedido.HistoricoStatusPedidoResponseDTO;
import io.github.JoasFyllipe.dto.itempedido.ItemPedidoResponseDTO;
import io.github.JoasFyllipe.model.pedido.Pedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoResponseDTO(
        Long id,
        LocalDateTime data,
        BigDecimal valorTotal,
        String situacaoAtual, // Alterado para refletir o status atual
        List<ItemPedidoResponseDTO> itens,
        List<HistoricoStatusPedidoResponseDTO> historico // Adicionada a lista de histórico
) {
    public static PedidoResponseDTO valueOf(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getData(),
                pedido.getValorTotal(),
                pedido.getSituacaoAtual().getDescricao(), // Pega a descrição do status atual
                pedido.getItens().stream().map(ItemPedidoResponseDTO::valueOf).collect(Collectors.toList()),
                pedido.getHistorico().stream().map(HistoricoStatusPedidoResponseDTO::valueOf).collect(Collectors.toList()) // Mapeia o histórico para DTO
        );
    }
}