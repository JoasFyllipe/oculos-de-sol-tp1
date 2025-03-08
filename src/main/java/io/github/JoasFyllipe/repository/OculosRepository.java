package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.Oculos;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OculosRepository implements PanacheRepository<Oculos> {
}
