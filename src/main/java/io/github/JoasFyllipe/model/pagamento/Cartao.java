package io.github.JoasFyllipe.model.pagamento;

import java.time.LocalDate;
import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Cartao extends DefaultEntity {

    // NOVO: Anotações @Column para mapeamento
    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private String cpfCartao;

    @Column(length = 16, nullable = false)
    private String numero;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Column(length = 100, nullable = false)
    private String cvc;

    // NOVO: @Enumerated para mapear o Enum corretamente no banco de dados
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModalidadeCartao modalidadeCartao;

    // NOVO E ESSENCIAL: Associação com o usuário dono do cartão
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Getters e Setters...
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getCpfCartao() { return cpfCartao; }
    public void setCpfCartao(String cpfCartao) { this.cpfCartao = cpfCartao; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }
    public ModalidadeCartao getModalidadeCartao() { return modalidadeCartao; }
    public void setModalidadeCartao(ModalidadeCartao modalidadeCartao) { this.modalidadeCartao = modalidadeCartao; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}