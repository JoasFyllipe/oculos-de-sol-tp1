package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.usuario.Cliente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente>{
 
    public List<Cliente> findByNome(String nome){
        return find("usuario.nome", nome).stream().toList();
    }
    public Cliente findByUsuario(String email){
        return find("usuario.email", email).firstResult();
    }

    public Cliente findByEmail(String email){
        return find("usuario.email", email).firstResult();
    }
}
