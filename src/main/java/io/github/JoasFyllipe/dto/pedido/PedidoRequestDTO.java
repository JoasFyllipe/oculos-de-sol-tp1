package io.github.JoasFyllipe.dto.pedido;

import io.github.JoasFyllipe.dto.itempedido.ItemPedidoRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequestDTO(
        @NotEmpty(message = "A lista de itens não pode estar vazia.")
        @Valid List<ItemPedidoRequestDTO> itens,

        @NotNull(message = "O ID do cartão para pagamento deve ser informado.")
        Long idCartao,

        @NotNull(message = "O ID do endereço de entrega deve ser informado.")
        Long idEndereco
) {}