package io.github.JoasFyllipe.model.marca;

import java.util.List;
import java.util.Objects;

import io.github.JoasFyllipe.exceptions.MarcaNotFoundException;
import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Marca extends DefaultEntity {

    @Column(length = 60, nullable = false)
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static Marca fromNome(String nome, List<Marca> marcas) {
        for (Marca m : marcas) {
            if (m.getNome().equalsIgnoreCase(nome))
                return m;
        }
        throw new MarcaNotFoundException("Marca não encontrada: " + nome);
    }

    public static Marca fromId(Long id, List<Marca> marcas) {
        for (Marca m : marcas) {
            if (Objects.equals(m.getId(), id))
                return m;
        }
        throw new MarcaNotFoundException("Marca não encontrada para o ID: " + id);
    }
}

