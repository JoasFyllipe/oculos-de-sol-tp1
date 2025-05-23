package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.marca.Marca;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarcaRepository implements PanacheRepository<Marca> {

    public Marca findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
}
