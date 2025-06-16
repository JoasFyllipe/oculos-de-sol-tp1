package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.usuario.endereco.Estado;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstadoRepository implements PanacheRepository<Estado> {

    // Consulta 1: Buscar um estado pela sua sigla exata.
    public Estado findBySigla(String sigla) {
        return find("UPPER(sigla) = ?1", sigla.toUpperCase()).firstResult();
    }

    // Consulta 2: Buscar estados cujo nome contenha uma determinada string (case-insensitive).
    public List<Estado> findByNomeLike(String nome) {
        return list("UPPER(nome) LIKE ?1", "%" + nome.toUpperCase() + "%");
    }

    // Consulta 3: Buscar todos os estados ordenados pelo nome.
    public List<Estado> findAllOrderedByName() {
        return list("ORDER BY nome");
    }
}