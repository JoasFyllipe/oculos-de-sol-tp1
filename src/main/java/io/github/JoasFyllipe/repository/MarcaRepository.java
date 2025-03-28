package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.Marca;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MarcaRepository implements PanacheRepository<Marca> {

    public Marca findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
}
