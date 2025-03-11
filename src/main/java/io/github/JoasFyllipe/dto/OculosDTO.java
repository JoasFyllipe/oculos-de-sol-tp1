package io.github.JoasFyllipe.dto;

import io.github.JoasFyllipe.model.Oculos;

public final class OculosDTO {
    private String nome;
    private Double valor;
    private Integer quantidadeEstoque;
    private Integer idCorArmacao;
    private Integer idGenero;
    private Integer idModelo;

    public OculosDTO(){
    }

    public OculosDTO(Oculos oculos) {
        this.nome = oculos.getNome();
        this.valor = oculos.getValor();
        this.quantidadeEstoque = oculos.getQuantidadeEstoque();
        this.idCorArmacao = (oculos.getCorArmacao() != null) ? oculos.getCorArmacao().getID() : null;
        this.idGenero = (oculos.getGenero() != null) ? oculos.getGenero().getID() : null;
        this.idModelo = (oculos.getModelo() != null) ? oculos.getModelo().getID() : null;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdCorArmacao() {
        return idCorArmacao;
    }

    public Integer getIdGenero() {
        return idGenero;
    }

    public Integer getIdModelo() {
        return idModelo;
    }

    public Double getValor() {
        return valor;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
}
