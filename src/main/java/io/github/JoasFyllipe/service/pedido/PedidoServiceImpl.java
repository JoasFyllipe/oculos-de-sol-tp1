package io.github.JoasFyllipe.service.pedido;

import io.github.JoasFyllipe.dto.atualizarsituacaopedido.AtualizacaoSituacaoRequestDTO;
import io.github.JoasFyllipe.dto.pagamento.BoletoResponseDTO;
import io.github.JoasFyllipe.dto.pagamento.PixResponseDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoRequestDTO;
import io.github.JoasFyllipe.dto.pedido.PedidoResponseDTO;
import io.github.JoasFyllipe.model.oculos.Oculos;
import io.github.JoasFyllipe.model.pagamento.Boleto;
import io.github.JoasFyllipe.model.pagamento.Cartao;
import io.github.JoasFyllipe.model.pagamento.CartaoPagamento; // Importe CartaoPagamento corretamente
import io.github.JoasFyllipe.model.pagamento.Pix;
import io.github.JoasFyllipe.model.usuario.endereco.EnderecoEntrega;
import io.github.JoasFyllipe.model.pedido.HistoricoStatusPedido;
import io.github.JoasFyllipe.model.pedido.Pedido;
import io.github.JoasFyllipe.model.pedido.itempedido.ItemPedido;
import io.github.JoasFyllipe.model.pedido.situacaopedido.SituacaoPedido;
import io.github.JoasFyllipe.model.usuario.Cliente;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;
import io.github.JoasFyllipe.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    @Inject ClienteRepository clienteRepository;
    @Inject PedidoRepository pedidoRepository;
    @Inject OculosRepository oculosRepository;
    @Inject CartaoRepository cartaoRepository;
    @Inject EnderecoRepository enderecoRepository;

    @Override
    @Transactional
    public PedidoResponseDTO create(String email, PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findByUsuario(email);
        if (cliente == null) {
            throw new NotFoundException("Cliente não encontrado para realizar o pedido.");
        }

        Endereco enderecoOrigem = enderecoRepository.findByUsuarioEmailAndId(email, dto.idEndereco());
        if (enderecoOrigem == null) {
            throw new ValidationException("Endereço com ID " + dto.idEndereco() + " não pertence a este usuário.");
        }

        // Cria o "Snapshot" do Endereço de Entrega
        EnderecoEntrega enderecoDeEntrega = new EnderecoEntrega();
        enderecoDeEntrega.setLogradouro(enderecoOrigem.getLogradouro());
        enderecoDeEntrega.setNumero(enderecoOrigem.getNumero());
        enderecoDeEntrega.setComplemento(enderecoOrigem.getComplemento());
        enderecoDeEntrega.setBairro(enderecoOrigem.getBairro());
        enderecoDeEntrega.setCep(enderecoOrigem.getCep());
        enderecoDeEntrega.setCidadeNome(enderecoOrigem.getCidade().getNome());
        enderecoDeEntrega.setEstadoNome(enderecoOrigem.getCidade().getEstado().getNome());
        enderecoDeEntrega.setEstadoSigla(enderecoOrigem.getCidade().getEstado().getSigla());

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoEntrega(enderecoDeEntrega);
        pedido.setSituacaoAtual(SituacaoPedido.AGUARDANDO_PAGAMENTO);
        pedido.getHistorico().add(new HistoricoStatusPedido(pedido, SituacaoPedido.AGUARDANDO_PAGAMENTO, "Pedido realizado."));

        List<ItemPedido> itens = dto.itens().stream().map(itemDto -> {
            Oculos oculos = oculosRepository.findByIdOptional(itemDto.idOculos())
                    .orElseThrow(() -> new NotFoundException("Óculos com ID " + itemDto.idOculos() + " não encontrado."));
            if (oculos.getQuantidadeEstoque() < itemDto.quantidade()) {
                throw new ValidationException("Estoque insuficiente para o produto: " + oculos.getNome());
            }
            oculos.setQuantidadeEstoque(oculos.getQuantidadeEstoque() - itemDto.quantidade());

            ItemPedido item = new ItemPedido();
            item.setOculos(oculos);
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoUnitario(oculos.getPreco());
            item.setPedido(pedido);
            return item;
        }).collect(Collectors.toList());

        pedido.setItens(itens);
        pedido.calcularValorTotal();

        // Processa o pagamento com cartão de crédito no momento da criação
        registrarPagamentoCartao(pedido, dto.idCartao(), email);

        pedidoRepository.persist(pedido);
        return PedidoResponseDTO.valueOf(pedido);
    }

    @Transactional
    public void registrarPagamentoCartao(Pedido pedido, Long idCartao, String email) {
        Cartao cartao = cartaoRepository.findByUsuarioEmailAndId(email, idCartao);
        if (cartao == null) {
            throw new ValidationException("Cartão com ID " + idCartao + " não pertence a este usuário.");
        }

        // Simulação de chamada a um gateway de pagamento. Se aprovado:
        // Crie uma instância de CartaoPagamento (não PagamentoCartao)
        CartaoPagamento pagamento = new CartaoPagamento();

        // Transfira os dados do cartão salvo para o registro do pagamento
        pagamento.setTitular(cartao.getTitular());
        pagamento.setCpfCartao(cartao.getCpfCartao());
        pagamento.setNumero(cartao.getNumero());
        pagamento.setDataValidade(cartao.getDataValidade());
        pagamento.setCvc(cartao.getCvc()); // Em um ambiente real, o CVC não seria salvo nem transferido desta forma
        pagamento.setModalidadeCartao(cartao.getModalidadeCartao());

        pagamento.setPedido(pedido);
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setDataProcessamento(LocalDateTime.now());

        pedido.setPagamento(pagamento); // Aqui o setPagamento deve funcionar, pois CartaoPagamento herda de Pagamento
        pedido.setSituacaoAtual(SituacaoPedido.PAGAMENTO_AUTORIZADO);
        pedido.getHistorico().add(new HistoricoStatusPedido(pedido, SituacaoPedido.PAGAMENTO_AUTORIZADO, "Pagamento com cartão de crédito aprovado."));
    }

    @Override
    public List<PedidoResponseDTO> findMinhasCompras(String email) {
        return pedidoRepository.findByClienteUsuarioEmail(email).stream()
                .map(PedidoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO findMinhaCompraById(String email, Long pedidoId) {
        Pedido pedido = pedidoRepository.findByClienteUsuarioEmailAndId(email, pedidoId);
        if (pedido == null) {
            throw new NotFoundException("Pedido com ID " + pedidoId + " não encontrado para este usuário.");
        }
        return PedidoResponseDTO.valueOf(pedido);
    }

    @Override
    @Transactional
    public void atualizarSituacao(Long pedidoId, AtualizacaoSituacaoRequestDTO dto, String emailOperador) {
        Pedido pedido = pedidoRepository.findByIdOptional(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido com ID " + pedidoId + " não encontrado."));

        SituacaoPedido novaSituacao = SituacaoPedido.valueOf(dto.idNovaSituacao());
        String observacao = dto.observacao() != null ? dto.observacao() : "Status alterado pelo operador: " + emailOperador;

        pedido.setSituacaoAtual(novaSituacao);
        pedido.getHistorico().add(new HistoricoStatusPedido(pedido, novaSituacao, observacao));
    }

    @Override
    @Transactional
    public void cancelarPedido(String email, Long pedidoId) {
        Pedido pedido = pedidoRepository.findByClienteUsuarioEmailAndId(email, pedidoId);
        if (pedido == null) {
            throw new NotFoundException("Pedido com ID " + pedidoId + " não encontrado para este usuário.");
        }

        if (pedido.getSituacaoAtual() == SituacaoPedido.ENVIADO || pedido.getSituacaoAtual() == SituacaoPedido.ENTREGUE) {
            throw new ValidationException("Não é possível cancelar um pedido que já foi enviado ou entregue.");
        }

        for (ItemPedido item : pedido.getItens()) {
            Oculos oculos = item.getOculos();
            oculos.setQuantidadeEstoque(oculos.getQuantidadeEstoque() + item.getQuantidade());
        }

        atualizarSituacao(pedidoId, new AtualizacaoSituacaoRequestDTO(SituacaoPedido.CANCELADO.getId(), "Pedido cancelado pelo cliente."), email);
    }

    @Override
    public PedidoResponseDTO findById(Long id) {
        return pedidoRepository.findByIdOptional(id)
                .map(PedidoResponseDTO::valueOf)
                .orElseThrow(() -> new NotFoundException("Pedido com ID " + id + " não encontrado."));
    }

    @Override
    public List<PedidoResponseDTO> findAll() {
        return pedidoRepository.listAll().stream()
                .map(PedidoResponseDTO::valueOf)
                .collect(Collectors.toList());
    }

    // Métodos de pagamento por Boleto e Pix (simulados)
    @Override
    @Transactional
    public BoletoResponseDTO gerarBoleto(Long pedidoId, String email) {
        Pedido pedido = pedidoRepository.findByClienteUsuarioEmailAndId(email, pedidoId);
        if (pedido == null) throw new NotFoundException("Pedido não encontrado.");

        Boleto boleto = new Boleto();
        boleto.setPedido(pedido);
        boleto.setValor(pedido.getValorTotal());
        boleto.setValidade(LocalDateTime.now().plusDays(3)); // Corrigido para .now()
        boleto.setCodigoBarras(UUID.randomUUID().toString().replace("-", ""));

        pedido.setPagamento(boleto);
        atualizarSituacao(pedidoId, new AtualizacaoSituacaoRequestDTO(SituacaoPedido.AGUARDANDO_PAGAMENTO.getId(), "Boleto gerado."), "SISTEMA");
        return BoletoResponseDTO.valueOf(boleto);
    }

    @Override
    @Transactional
    public PixResponseDTO gerarCodigoPix(Long pedidoId, String email) {
        Pedido pedido = pedidoRepository.findByClienteUsuarioEmailAndId(email, pedidoId);
        if (pedido == null) throw new NotFoundException("Pedido não encontrado.");

        Pix pix = new Pix();
        pix.setPedido(pedido);
        pix.setValor(pedido.getValorTotal());
        pix.setValidade(LocalDateTime.now().plusMinutes(30)); // Corrigido para .now()
        pix.setChave(UUID.randomUUID().toString());

        pedido.setPagamento(pix);
        atualizarSituacao(pedidoId, new AtualizacaoSituacaoRequestDTO(SituacaoPedido.AGUARDANDO_PAGAMENTO.getId(), "Código PIX gerado."), "SISTEMA");
        return PixResponseDTO.valueOf(pix);
    }

    @Override
    @Transactional
    public void registrarPagamentoBoleto(Long idPedido) {
        atualizarSituacao(idPedido, new AtualizacaoSituacaoRequestDTO(SituacaoPedido.PAGAMENTO_AUTORIZADO.getId(), "Pagamento do boleto compensado."), "GATEWAY");
    }

    @Override
    @Transactional
    public void registrarPagamentoPix(Long idPedido) {
        atualizarSituacao(idPedido, new AtualizacaoSituacaoRequestDTO(SituacaoPedido.PAGAMENTO_AUTORIZADO.getId(), "Pagamento PIX confirmado."), "GATEWAY");
    }
}