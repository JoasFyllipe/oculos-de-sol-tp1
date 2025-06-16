package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.usuario.endereco.Cidade;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CidadeRepository implements PanacheRepository<Cidade> {

    // Buscar cidades por nome (case-insensitive).
    public List<Cidade> findByNome(String nome) {
        return list("UPPER(nome) = ?1", nome.toUpperCase());
    }

    // Buscar todas as cidades de um determinado estado pelo ID do estado.
    public List<Cidade> findByEstadoId(Long estadoId) {
        return list("estado.id = ?1", estadoId);
    }

    //  Buscar todas as cidades de um determinado estado pela sigla do estado.
    public List<Cidade> findByEstadoSigla(String sigla) {
        return list("UPPER(estado.sigla) = ?1", sigla.toUpperCase());
    }
}