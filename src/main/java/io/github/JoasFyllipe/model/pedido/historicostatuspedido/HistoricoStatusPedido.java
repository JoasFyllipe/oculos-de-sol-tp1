package io.github.JoasFyllipe.model.pedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.pedido.situacaopedido.SituacaoPedido;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status_pedido")
public class HistoricoStatusPedido extends DefaultEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoPedido situacao;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(length = 255)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    public HistoricoStatusPedido() {
        this.dataHora = LocalDateTime.now();
    }

    public HistoricoStatusPedido(Pedido pedido, SituacaoPedido situacao, String observacao) {
        this();
        this.pedido = pedido;
        this.situacao = situacao;
        this.observacao = observacao;
    }

    // Getters e Setters
    public SituacaoPedido getSituacao() { return situacao; }
    public void setSituacao(SituacaoPedido situacao) { this.situacao = situacao; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}