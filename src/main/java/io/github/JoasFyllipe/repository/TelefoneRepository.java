package io.github.JoasFyllipe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TelefoneRepository implements PanacheRepository<Telefone> {

    // Método que busca um telefone pelo seu ID e pelo email do usuário dono.
    public Telefone findByUsuarioEmailAndId(String email, Long telefoneId) {
        return find("usuario.email = ?1 and id = ?2", email, telefoneId).firstResult();
    }
}