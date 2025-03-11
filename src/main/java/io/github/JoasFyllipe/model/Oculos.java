package io.github.JoasFyllipe.model;

import io.github.JoasFyllipe.model.converterjpa.CorArmacaoConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "oculos")
public class Oculos {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long id;

    @Column(length = 60, nullable = false)
    public String nome;

    @Column(length = 60, nullable = false)
    public Double valor;

    @Column(length = 60, nullable = false)
    public Integer quantidadeEstoque;

    @Convert(converter = CorArmacaoConverter.class)
    private CorArmacao corArmacao;
    @Enumerated(EnumType.ORDINAL)
    private Genero genero;
    @Enumerated(EnumType.ORDINAL)
    private Modelo modelo;


    public Long getId() {
        return id;
    }

    public CorArmacao getCorArmacao() {
        return corArmacao;
    }

    public void setCorArmacao(CorArmacao corArmacao) {
        this.corArmacao = corArmacao;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }
}
