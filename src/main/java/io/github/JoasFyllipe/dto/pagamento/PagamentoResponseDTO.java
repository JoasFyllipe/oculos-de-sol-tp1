package io.github.JoasFyllipe.dto.pagamento;


import io.github.JoasFyllipe.model.pagamento.Boleto;
import io.github.JoasFyllipe.model.pagamento.CartaoPagamento;
import io.github.JoasFyllipe.model.pagamento.Pagamento;
import io.github.JoasFyllipe.model.pagamento.Pix;

public record PagamentoResponseDTO(Object Pagamento) {

    public static Object valueOf(Pagamento pagamento) {
        if (pagamento instanceof Boleto) {
            Boleto boleto = (Boleto) pagamento;
            return BoletoResponseDTO.valueOf(boleto);
        }

        if (pagamento instanceof Pix) {
            Pix pix = (Pix) pagamento;
            return PixResponseDTO.valueOf(pix);
        }

        if (pagamento instanceof CartaoPagamento) {
            CartaoPagamento cartao = (CartaoPagamento) pagamento;
            return CartaoResponseDTO.valueOf(cartao);
        }

        return null;
    }

}
