package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.Marca;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MarcaRepository implements PanacheRepository<Marca> {

    public List<Marca> findByNomeMarca(String nome){
        return find("SELECT m FROM Marca m WHERE m.nome LIKE ?1", "%" + nome + "%").list();
    }
}
