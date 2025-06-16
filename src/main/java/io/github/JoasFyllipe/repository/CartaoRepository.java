package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.pagamento.Cartao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CartaoRepository implements PanacheRepository<Cartao> {

    public Cartao findByUsuarioEmailAndId(String email, Long cartaoId) {
        return find("usuario.email = ?1 and id = ?2", email, cartaoId).firstResult();
    }

    public List<Cartao> findByUsuarioEmail(String email) {
        return list("usuario.email", email);
    }
}