package io.github.JoasFyllipe.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genero {
    MASCULINO(1, "Masculino"),
    FEMININO(2, "Feminino"),
    UNISEX(3, "Unisex");

    private final int ID;
    private final String NOME;

    Genero(int ID, String NOME) {
        this.ID = ID;
        this.NOME = NOME;
    }

    public int getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static Genero valueOf(int id){
        for(Genero g: Genero.values()){
            if(g.getID() == id)
                return g;
        }
        return null;
    }
}
