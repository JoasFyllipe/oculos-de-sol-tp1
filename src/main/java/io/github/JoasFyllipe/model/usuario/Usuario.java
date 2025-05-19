package io.github.JoasFyllipe.model.usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity
public class Usuario extends DefaultEntity {

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(length = 15, nullable = false)
    private String telefone;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public static Usuario fromId(Long id, List<Usuario> usuarios) {
        for (Usuario u : usuarios) {
            if (Objects.equals(u.getId(), id))
                return u;
        }
        throw new RuntimeException("Usuário não encontrado para o ID: " + id);
    }
}
