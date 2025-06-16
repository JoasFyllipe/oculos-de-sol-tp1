package io.github.JoasFyllipe.model.usuario;

public enum Perfil {

    ADM(1, "Adm"),
    EMPLOYE(2, "Employe"),
    USER(3, "User");

    private final Integer id;
    private final String label;

    Perfil(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static Perfil valueOf(Integer id){
        if(id == null)
            return null;
        for(Perfil perfil : Perfil.values()){
            if (perfil.getId().equals(id)){
                return perfil;
            }
        }
        throw new IllegalArgumentException("Id inválido");
    }

}
