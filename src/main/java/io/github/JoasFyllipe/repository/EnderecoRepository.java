package io.github.JoasFyllipe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import io.github.JoasFyllipe.model.endereco.Endereco;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class EnderecoRepository implements PanacheRepository<Endereco> {
    // Métodos personalizados podem ser adicionados aqui, se necessário
}
