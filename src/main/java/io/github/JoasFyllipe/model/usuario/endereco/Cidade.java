package io.github.JoasFyllipe.model.usuario.endereco;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import jakarta.persistence.*;

@Entity
public class Cidade extends DefaultEntity {
    
    @Column(length = 100, nullable = false)
    private String nome;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    
}
