package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.funcionario.Funcionario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FuncionarioRepository implements PanacheRepository<Funcionario> {
    public Funcionario findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
}
