package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.usuario.endereco.EnderecoEntrega;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnderecoEntregaRepository implements PanacheRepository<EnderecoEntrega> {
}