package io.github.JoasFyllipe.dto;

public final class OculosDTO {
    private String nome;
    private Double valor;
    private Integer quantidadeEstoque;
    private Long idCorArmacao;
    private Long idGenero;
    private Long idModelo;

    public OculosDTO(){
    }

    public OculosDTO(String nome, Double valor, Integer quantidadeEstoque, Long idCorArmacao, Long idGenero, Long idModelo) {
        this.nome = nome;
        this.valor = valor;
        this.quantidadeEstoque = quantidadeEstoque;
        this.idCorArmacao = idCorArmacao;
        this.idGenero = idGenero;
        this.idModelo = idModelo;
    }


    public String getNome() {
        return nome;
    }

    public Long getIdCorArmacao() {
        return idCorArmacao;
    }

    public Long getIdGenero() {
        return idGenero;
    }

    public Long getIdModelo() {
        return idModelo;
    }

    public Double getValor() {
        return valor;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }
}
