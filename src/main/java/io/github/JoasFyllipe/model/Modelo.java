package io.github.JoasFyllipe.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.JoasFyllipe.model.exceptions.ModelNotFoundException;

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

    public static Modelo fromNome(String nome){
        for(Modelo m: Modelo.values()){
            if(m.getNOME().equalsIgnoreCase(nome))
                return m;
        }
        throw new ModelNotFoundException("Cor não encontrada: " + nome);
    }

    public static Modelo fromId(int id){
        for(Modelo m: Modelo.values()){
            if(m.getID() == id)
                return m;
        }
        throw new ModelNotFoundException("Cor não encontrada para o ID: "+ id);
    }

}
