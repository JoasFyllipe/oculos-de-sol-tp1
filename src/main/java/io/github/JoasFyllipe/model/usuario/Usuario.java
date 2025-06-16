package io.github.JoasFyllipe.model.usuario;

import java.time.LocalDate;
import java.util.List;
import io.github.JoasFyllipe.model.defaultentity.DefaultEntity;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;
import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import jakarta.persistence.*;

@Entity
public class Usuario extends DefaultEntity {

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String senha;

    // CORREÇÃO: Mapeamento para a coleção de Enums.
    // O nome da tabela de junção é 'usuario_perfis' e a coluna que guarda o valor do enum é 'perfil'.
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.ORDINAL)
    @CollectionTable(name = "usuario_perfis", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "perfil", nullable = false)
    private List<Perfil> perfis;

    // CORREÇÃO: Removido @JoinTable e adicionado mappedBy.
    // Isso instrui o Hibernate a usar o campo 'usuario' da entidade Endereco para o mapeamento.
    // Não cria a tabela extra 'endereco_usuario'.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos;

    // CORREÇÃO: Mesma lógica para Telefone.
    // Não cria a tabela extra 'telefone_usuario'.
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Telefone> telefones;

    // Getters e Setters (não precisam de mudança)
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public List<Perfil> getPerfis() { return perfis; }
    public void setPerfis(List<Perfil> perfis) { this.perfis = perfis; }
    public List<Endereco> getEndereco() { return enderecos; }
    public void setEndereco(List<Endereco> enderecos) { this.enderecos = enderecos; }
    public List<Telefone> getTelefone() { return telefones; }
    public void setTelefone(List<Telefone> telefones) { this.telefones = telefones; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}