package io.github.JoasFyllipe.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import io.github.JoasFyllipe.model.usuario.Usuario;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Usuario findByNome(String nome) {
        return find("nome", nome).firstResult();
    }

    public List<Usuario> findByEmail(String email) {
        return list("email", email);
    }

    public List<Usuario> findByIdade(int idade) {
        return list("idade", idade);
    }
}
