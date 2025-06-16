package io.github.JoasFyllipe.repository;
import io.github.JoasFyllipe.model.pedido.HistoricoStatusPedido;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HistoricoStatusPedidoRepository implements PanacheRepository<HistoricoStatusPedido> {}