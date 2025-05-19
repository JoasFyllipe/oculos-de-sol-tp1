package io.github.JoasFyllipe.model.telefone;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Telefone extends DefaultEntity {

    @Column(length = 15, nullable = false)
    private String numero;

    @Column(length = 20, nullable = false)
    private String tipo; // Ex: "Celular", "Residencial", etc.

    @Column(nullable = false)
    private boolean principal;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }
}
