package io.github.JoasFyllipe.dto.pagamento;

import io.github.JoasFyllipe.model.pagamento.Cartao;
import io.github.JoasFyllipe.model.pagamento.CartaoPagamento;
import io.github.JoasFyllipe.model.pagamento.ModalidadeCartao;

import java.time.LocalDate; // ADICIONADO
import java.time.format.DateTimeFormatter; // ADICIONADO

public record CartaoResponseDTO(
        Long id,
        String titular,
        String numero,
        String validade,
        ModalidadeCartao modalidadeCartao
) {

    public static CartaoResponseDTO valueOf(Cartao cartao) {
        if (cartao == null) {
            return null;
        }
        return new CartaoResponseDTO(
                cartao.getId(),
                cartao.getTitular(),
                ofuscarNumero(cartao.getNumero()),
                converterData(cartao.getDataValidade()),
                cartao.getModalidadeCartao());
    }

    public static CartaoResponseDTO valueOf(CartaoPagamento cartao) {
        if (cartao == null) {
            return null;
        }
        return new CartaoResponseDTO(
                cartao.getId(),
                cartao.getTitular(),
                ofuscarNumero(cartao.getNumero()),
                converterData(cartao.getDataValidade()),
                cartao.getModalidadeCartao());
    }

    private static String ofuscarNumero(String numero) {
        if (numero == null || numero.length() < 16) {
            return "**** **** **** ****"; // Retorna uma máscara padrão se o número for inválido
        }
        return "**** **** **** " + numero.substring(numero.length() - 4);
    }

    private static String converterData(LocalDate validade) {
        if (validade == null) {
            return null;
        }
        return validade.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }
}
