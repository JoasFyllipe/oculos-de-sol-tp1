package io.github.JoasFyllipe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.List;

@ApplicationScoped
public class EnderecoRepository implements PanacheRepository<Endereco> {

    public List<Endereco> findByUsuarioEmail(String email) {
        return list("usuario.email", email);
    }

    public Endereco findByUsuarioEmailAndId(String email, Long enderecoId) {
        return find("usuario.email = ?1 and id = ?2", email, enderecoId).firstResult();
    }
}