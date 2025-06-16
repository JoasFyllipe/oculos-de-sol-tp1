package io.github.JoasFyllipe.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.JoasFyllipe.exceptions.GenderNotFoundException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Genero {
    MASCULINO(1, "Masculino"),
    FEMININO(2, "Feminino"),
    UNISEX(3, "Unisex");

    private final int id;
    private final String label;

    Genero(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static Genero valueOf(int id){
        for(Genero g: Genero.values()){
            if(g.getId() == id)
                return g;
        }
        return null;
    }

    public static Genero fromNome(String nome){
        for(Genero g: Genero.values()){
            if(g.getLabel().equalsIgnoreCase(nome))
                return g;
        }
        throw new GenderNotFoundException("Gênero não encontrada: " + nome);
    }

    public static Genero fromId(int id){
        for(Genero g: Genero.values()){
            if(g.getId() == id)
                return g;
        }
        throw new GenderNotFoundException("Gênero não encontrada para o ID: "+ id);
    }
}
