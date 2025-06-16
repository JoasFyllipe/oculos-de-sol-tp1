package io.github.JoasFyllipe.model.usuario.telefone; // (Ou em um pacote de enums: io.github.JoasFyllipe.model.enums)

public enum TipoTelefone {
    CELULAR(1, "Celular"),
    RESIDENCIAL(2, "Residencial"),
    COMERCIAL(3, "Comercial");

    private final int id;
    private final String label;

    TipoTelefone(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static TipoTelefone valueOf(Integer id) {
        if (id == null) {
            return null;
        }
        for (TipoTelefone tipo : TipoTelefone.values()) {
            if (tipo.getId() == id) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("ID de TipoTelefone inválido: " + id);
    }
}