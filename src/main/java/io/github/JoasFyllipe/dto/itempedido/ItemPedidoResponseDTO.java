package io.github.JoasFyllipe.dto.itempedido;

import io.github.JoasFyllipe.model.oculos.Oculos;
import io.github.JoasFyllipe.model.pedido.itempedido.ItemPedido;
import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long id,
        Long idOculos,
        String nomeOculos,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static ItemPedidoResponseDTO valueOf(ItemPedido itemPedido) {
        Oculos oculos = itemPedido.getOculos();
        return new ItemPedidoResponseDTO(
                itemPedido.getId(),
                oculos.getId(),
                oculos.getNome(),
                itemPedido.getQuantidade(),
                itemPedido.getPrecoUnitario(),
                itemPedido.getPrecoUnitario().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()))
        );
    }
}