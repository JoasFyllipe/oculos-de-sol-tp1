package io.github.JoasFyllipe.model.pagamento;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CARTAO")
public class CartaoPagamento extends Pagamento {

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private String cpfCartao;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Column(nullable = false)
    private String cvc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadeCartao modalidadeCartao;

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCpfCartao() {
        return cpfCartao;
    }

    public void setCpfCartao(String cpfCartao) {
        this.cpfCartao = cpfCartao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public ModalidadeCartao getModalidadeCartao() {
        return modalidadeCartao;
    }

    public void setModalidadeCartao(ModalidadeCartao modalidadeCartao) {
        this.modalidadeCartao = modalidadeCartao;
    }

}
