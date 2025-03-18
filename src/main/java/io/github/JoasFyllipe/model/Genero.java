package io.github.JoasFyllipe.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.JoasFyllipe.model.exceptions.GenderNotFoundException;

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

    public static Genero fromNome(String nome){
        for(Genero g: Genero.values()){
            if(g.getNOME().equalsIgnoreCase(nome))
                return g;
        }
        throw new GenderNotFoundException("Gênero não encontrada: " + nome);
    }

    public static Genero fromId(int id){
        for(Genero g: Genero.values()){
            if(g.getID() == id)
                return g;
        }
        throw new GenderNotFoundException("Gênero não encontrada para o ID: "+ id);
    }
}
