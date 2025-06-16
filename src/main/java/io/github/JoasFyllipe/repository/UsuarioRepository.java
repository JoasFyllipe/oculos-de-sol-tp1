package io.github.JoasFyllipe.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import io.github.JoasFyllipe.model.usuario.Usuario;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Usuario findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }

    public Usuario findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public Usuario findByEmail(String email){
        return find("email", email).firstResult();
    }

    public Usuario findByEmailAndSenha(String email, String senha) {
        return find("email = ?1 and senha = ?2", email, senha).firstResult();
    }


}
