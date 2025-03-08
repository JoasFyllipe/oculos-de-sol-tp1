package io.github.JoasFyllipe.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CorArmacao {
    PRETO(1, "Preto"),
    DOURADO(2, "Dourado"),
    PRATA(3, "Prata");

    private final int ID;
    private final String NOME;

    CorArmacao(int ID, String NOME) {
        this.ID = ID;
        this.NOME = NOME;
    }

    public int getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static CorArmacao valueOf(int id){
        for(CorArmacao c: CorArmacao.values()){
            if(c.getID() == id)
                return c;
        }
        return null;
    }
}
