package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.enums.CorArmacao;
import io.github.JoasFyllipe.model.enums.Genero;
import io.github.JoasFyllipe.model.enums.Modelo;
import io.github.JoasFyllipe.model.marca.Marca;
import io.github.JoasFyllipe.model.oculos.Oculos;
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
    public List<Oculos> findByMarca(Marca marca){
        return list("marca", marca);
    }
}
