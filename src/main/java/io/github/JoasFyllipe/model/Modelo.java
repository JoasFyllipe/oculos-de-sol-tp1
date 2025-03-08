package io.github.JoasFyllipe.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Modelo {
    AVIADOR(1, "Aviador"),
    ESPORTIVO(2, "Esportivo"),
    RETRO(3, "Retrô");

    private final int ID;
    private final String NOME;

    private Modelo(int id, String nome) {
        this.ID = id;
        this.NOME = nome;
    }

    public int getID() {
        return ID;
    }

    public String getNOME() {
        return NOME;
    }

    public static Modelo valueOf(int id){
        for(Modelo m: Modelo.values()){
            if(m.getID() == id)
                return m;
        }
        return null;
    }
}
