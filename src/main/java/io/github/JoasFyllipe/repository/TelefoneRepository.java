package io.github.JoasFyllipe.repository;

import jakarta.enterprise.context.ApplicationScoped;
import io.github.JoasFyllipe.model.telefone.Telefone;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class TelefoneRepository implements PanacheRepository<Telefone> {
    // Custom query methods can be added here if needed
    
}
