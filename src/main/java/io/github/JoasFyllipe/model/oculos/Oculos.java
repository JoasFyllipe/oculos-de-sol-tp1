package io.github.JoasFyllipe.model.oculos;

import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.enums.CorArmacao;
import io.github.JoasFyllipe.model.enums.Genero;
import io.github.JoasFyllipe.model.enums.Modelo;
import io.github.JoasFyllipe.model.marca.Marca;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "oculos")
public class Oculos extends DefaultEntity {

    @Column(length = 60, nullable = false)
    private String nome;

    @Column(length = 60, nullable = false)
    private Double valor;

    @Column(length = 60, nullable = false)
    private Integer quantidadeEstoque;

    private CorArmacao corArmacao;
    private Genero genero;
    private Modelo modelo;

    @ManyToOne
    @JoinColumn(name="id_marca")
    private Marca marca;

    // Construtor padrão
    public Oculos() {
    }

    // Construtor com argumentos
    public Oculos(String nome, Double valor, Integer quantidadeEstoque, CorArmacao corArmacao, Genero genero, Modelo modelo, Marca marca) {
        this.nome = nome;
        this.valor = valor;
        this.quantidadeEstoque = quantidadeEstoque;
        this.corArmacao = corArmacao;
        this.genero = genero;
        this.modelo = modelo;
        this.marca = marca;
    }

    public Marca getMarca(){
        return marca;
    }
    public void setMarca(Marca marca){
        this.marca = marca;
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
