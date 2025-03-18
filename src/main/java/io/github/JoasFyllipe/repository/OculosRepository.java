package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Genero;
import io.github.JoasFyllipe.model.Modelo;
import io.github.JoasFyllipe.model.Oculos;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class OculosRepository implements PanacheRepository<Oculos> {

    public List<Oculos> findByCor(CorArmacao corArmacao){
        return list("corArmacao", corArmacao);
    }
    public List<Oculos> findByGenero(Genero genero) {
        return list("genero", genero);
    }
    public List<Oculos> findByModelo(Modelo modelo){
        return list("modelo", modelo);
    }
}
