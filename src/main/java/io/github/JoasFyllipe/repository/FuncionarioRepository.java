package io.github.JoasFyllipe.repository;

import io.github.JoasFyllipe.model.usuario.Funcionario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class FuncionarioRepository implements PanacheRepository<Funcionario> {

    // CORRIGIDO: Busca pelo nome do usuário associado
    public Funcionario findByNome(String nome) {
        return find("usuario.nome", nome).firstResult();
    }

    // CORRIGIDO: Busca pelo email do usuário associado
    public Funcionario findByUsuario(String email){
        return find("usuario.email", email).firstResult();
    }

    // CORRIGIDO: Busca pelo email do usuário associado
    public Funcionario findByEmail(String email){
        return find("usuario.email", email).firstResult();
    }

    // Este método já estava correto
    public List<Funcionario> findByCargo(String cargo){
        return find("cargo", cargo).list();
    }
}