package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.pedido.Pedido;
import io.github.JoasFyllipe.model.pedido.situacaopedido.SituacaoPedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {

    public Pedido findByClienteUsuarioEmailAndId(String email, Long pedidoId) {
        return find("cliente.usuario.email = ?1 and id = ?2", email, pedidoId).firstResult();
    }

    public List<Pedido> findByClienteUsuarioEmail(String email) {
            return list("cliente.usuario.email = ?1 order by data desc", email);
        }

    public List<Pedido> findBySituacao(SituacaoPedido situacao) {
        return list("situacaoAtual", situacao);
    }
}
