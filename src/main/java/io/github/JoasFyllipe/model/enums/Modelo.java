package io.github.JoasFyllipe.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.JoasFyllipe.exceptions.ModelNotFoundException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Modelo {
    AVIADOR(1, "Aviador"),
    ESPORTIVO(2, "Esportivo"),
    RETRO(3, "Retrô");

    private final Integer id;
    private final String label;

    Modelo(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static Modelo valueOf(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do modelo não pode ser nulo.");
        }
        for (Modelo m : Modelo.values()) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        throw new IllegalArgumentException("ID de Modelo inválido: " + id);
    }

    public static Modelo fromNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do modelo não pode ser nulo ou vazio.");
        }
        for (Modelo m : Modelo.values()) {
            if (m.getLabel().equalsIgnoreCase(nome)) {
                return m;
            }
        }
        throw new ModelNotFoundException("Modelo não encontrado: " + nome);
    }
}