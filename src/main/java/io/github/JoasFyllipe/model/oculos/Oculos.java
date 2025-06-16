package io.github.JoasFyllipe.model.oculos;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.enums.CorArmacao;
import io.github.JoasFyllipe.model.enums.Genero;
import io.github.JoasFyllipe.model.enums.Modelo;
import io.github.JoasFyllipe.model.marca.Marca;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "oculos")
public class Oculos extends DefaultEntity {

    @Column(length = 60, nullable = false)
    private String nome;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco; // Renomeado de 'valor' para 'preco' por clareza

    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CorArmacao corArmacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modelo modelo;

    @ManyToOne(optional = false)
    @JoinColumn(name="id_marca")
    private Marca marca;


    public BigDecimal getPreco() {
        return preco;
    }
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
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

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}