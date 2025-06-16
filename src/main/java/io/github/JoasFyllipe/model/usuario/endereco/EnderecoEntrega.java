package io.github.JoasFyllipe.model.usuario.endereco;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class EnderecoEntrega extends DefaultEntity {

    @Column(nullable = false, length = 100)
    private String logradouro;

    @Column(nullable = false, length = 10)
    private String numero;

    @Column(length = 50)
    private String complemento;

    @Column(nullable = false, length = 50)
    private String bairro;

    @Column(nullable = false, length = 50)
    private String cidadeNome;

    @Column(nullable = false, length = 2)
    private String estadoSigla;

    @Column(nullable = false, length = 50)
    private String estadoNome;

    @Column(nullable = false, length = 8)
    private String cep;

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidadeNome() {return cidadeNome;}

    public void setCidadeNome(String cidadeNome) {this.cidadeNome = cidadeNome;}

    public String getEstadoSigla() {return estadoSigla;}

    public void setEstadoSigla(String estadoSigla) {this.estadoSigla = estadoSigla;}

    public String getEstadoNome() {
        return estadoNome;
    }

    public void setEstadoNome(String estadoNome) {
        this.estadoNome = estadoNome;
    }
}
