package io.github.JoasFyllipe.model.pedido;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.pagamento.Pagamento;
import io.github.JoasFyllipe.model.pedido.HistoricoStatusPedido;
import io.github.JoasFyllipe.model.pedido.itempedido.ItemPedido;
import io.github.JoasFyllipe.model.pedido.situacaopedido.SituacaoPedido;
import io.github.JoasFyllipe.model.usuario.Cliente;
import io.github.JoasFyllipe.model.usuario.endereco.EnderecoEntrega;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido extends DefaultEntity {

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_atual", nullable = false)
    private SituacaoPedido situacaoAtual;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HistoricoStatusPedido> historico = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_endereco_entrega", nullable = false)
    private EnderecoEntrega enderecoEntrega;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Pagamento pagamento;

    public Pedido() {
        this.valorTotal = BigDecimal.ZERO;
        this.data = LocalDateTime.now();
    }

    public void calcularValorTotal() {
        this.valorTotal = this.itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public SituacaoPedido getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(SituacaoPedido situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public List<io.github.JoasFyllipe.model.pedido.HistoricoStatusPedido> getHistorico() {
        return historico;
    }

    public void setHistorico(List<io.github.JoasFyllipe.model.pedido.HistoricoStatusPedido> historico) {
        this.historico = historico;
    }

    public EnderecoEntrega getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
}