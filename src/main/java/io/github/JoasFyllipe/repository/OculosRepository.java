package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.dto.OculosDTO;
import io.github.JoasFyllipe.model.CorArmacao;
import io.github.JoasFyllipe.model.Oculos;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class OculosRepository implements PanacheRepository<Oculos> {

    public List<Oculos> findByCor(CorArmacao corArmacao){
        return list("corArmacao", corArmacao);
    }
}
