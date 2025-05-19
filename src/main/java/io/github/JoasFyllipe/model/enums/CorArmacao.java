package io.github.JoasFyllipe.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.JoasFyllipe.exceptions.ColorNotFoundException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CorArmacao {
    PRETO(1, "Preto"),
    DOURADO(2, "Dourado"),
    PRATA(3, "Prata");

    private final Integer ID;
    private final String NOME;

    CorArmacao(int ID, String NOME) {
        this.ID = ID;
        this.NOME = NOME;
    }

    public Integer getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }


    public static CorArmacao fromNome(String nome){
        for(CorArmacao c: CorArmacao.values()){
            if(c.getNOME().equalsIgnoreCase(nome))
                return c;
        }
        throw new ColorNotFoundException("Cor não encontrada: " + nome);
    }

    public static CorArmacao fromId(int id){
        for(CorArmacao c: CorArmacao.values()){
            if(c.getID() == id)
                return c;
        }
        throw new ColorNotFoundException("Cor não encontrada para o ID: "+ id);
    }

    public static CorArmacao valueOf(int id) {
        for (CorArmacao c : CorArmacao.values()) {
            if (c.getID() == id)
                return c;
        }
        return null;
    }
}
