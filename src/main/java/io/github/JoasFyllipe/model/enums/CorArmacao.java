package io.github.JoasFyllipe.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.JoasFyllipe.exceptions.ColorNotFoundException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CorArmacao {
    PRETO(1, "Preto"),
    MARROM(2, "Marrom"),
    AZUL(3, "Azul"),
    VERMELHO(4, "Vermelho"),
    DOURADO(5, "Dourado");

    private final Integer id;
    private final String label;

    CorArmacao(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static CorArmacao valueOf(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID da cor não pode ser nulo.");
        }
        for (CorArmacao cor : CorArmacao.values()) {
            if (cor.getId().equals(id)) {
                return cor;
            }
        }

        throw new IllegalArgumentException("ID de Cor da Armação inválido: " + id);
    }

    public static CorArmacao fromNome(String nome){
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da cor não pode ser nulo ou vazio.");
        }
        for(CorArmacao c: CorArmacao.values()){
            if(c.getLabel().equalsIgnoreCase(nome))
                return c;
        }
        throw new ColorNotFoundException("Cor não encontrada: " + nome);
    }
}