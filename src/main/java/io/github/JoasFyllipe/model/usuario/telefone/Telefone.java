package io.github.JoasFyllipe.model.usuario.telefone;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.usuario.Usuario; // NOVO: Import da entidade Usuario
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // NOVO
import jakarta.persistence.Enumerated; // NOVO
import jakarta.persistence.JoinColumn; // NOVO
import jakarta.persistence.ManyToOne; // NOVO

@Entity
public class Telefone extends DefaultEntity {

    @Column(length = 9, nullable = false) // Ajustado para refletir a validação (8-9 dígitos)
    private String numero;

    @Column(length = 2, nullable = false) // Ajustado para refletir a validação (2 dígitos)
    private String ddd;

    @Column(nullable = false)
    private boolean principal;

    @Enumerated(EnumType.STRING) // Armazena o nome do enum ("CELULAR") no banco, que é mais legível.
    @Column(nullable = false)
    private TipoTelefone tipo;

    // NOVO: Relacionamento para ligar o telefone a um usuário. Essencial para a segurança.
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;


    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public TipoTelefone getTipo() {
        return tipo;
    }

    public void setTipo(TipoTelefone tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}