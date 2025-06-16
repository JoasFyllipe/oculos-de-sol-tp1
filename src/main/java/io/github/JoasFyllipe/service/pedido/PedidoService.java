package io.github.JoasFyllipe.service.pedido;

import io.github.JoasFyllipe.dto.atualizarsituacaopedido.AtualizacaoSituacaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.BoletoResponseDTO;
import io.github.JoasFyllipe.dto.pagamento.PixResponseDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoRequestDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoResponseDTO;
import io.github.JoasFyllipe.model.pedido.Pedido;

import java.util.List;

public interface PedidoService {

    // --- Métodos para Clientes ---
    PedidoResponseDTO create(String email, PedidoRequestDTO dto);
    List<PedidoResponseDTO> findMinhasCompras(String email);
    PedidoResponseDTO findMinhaCompraById(String email, Long pedidoId);
    void cancelarPedido(String email, Long pedidoId);

    // --- Métodos de Pagamento ---
    BoletoResponseDTO gerarBoleto(Long pedidoId, String email);
    PixResponseDTO gerarCodigoPix(Long pedidoId, String email);
    void registrarPagamentoBoleto(Long idPedido);
    void registrarPagamentoPix(Long idPedido);

    // --- Métodos para Administração ---
    void atualizarSituacao(Long pedidoId, AtualizacaoSituacaoRequestDTO dto, String emailOperador);
    PedidoResponseDTO findById(Long id);
    List<PedidoResponseDTO> findAll();
}