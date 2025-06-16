package io.github.JoasFyllipe.dto.atualizarsituacaopedido;

import jakarta.validation.constraints.NotNull;

public record AtualizacaoSituacaoRequestDTO(
        @NotNull(message = "O ID da nova situação do pedido não pode ser nulo.")
        Integer idNovaSituacao,

        String observacao
) {}