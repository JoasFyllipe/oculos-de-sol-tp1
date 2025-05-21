package io.github.JoasFyllipe.dto.funcionario;

import java.time.LocalDate;

import io.github.JoasFyllipe.model.funcionario.Funcionario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FuncionarioResponseDTO {

    private Long id;

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    private String telefone;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Cargo não pode estar em branco")
    @Size(max = 50, message = "Cargo deve ter no máximo 50 caracteres")
    private String cargo;

    private LocalDate dataContratacao;

    // Construtores, getters e setters

    public FuncionarioResponseDTO() {
    }

    public FuncionarioResponseDTO(Long id, String nome, String cpf, String telefone, String email, String cargo, LocalDate dataContratacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cargo = cargo;
        this.dataContratacao = dataContratacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDate getDataContratacao() {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }

    // Método para converter um Funcionario em FuncionarioResponseDTO
    public static FuncionarioResponseDTO valueOf(Funcionario funcionario) {
        if (funcionario == null) {
            return null;
        }
        return new FuncionarioResponseDTO(
            funcionario.getId(),
            funcionario.getUsuario().getNome(),
            funcionario.getUsuario().getCpf(),
            funcionario.getUsuario().getTelefone(),
            funcionario.getUsuario().getEmail(),
            funcionario.getCargo(),
            funcionario.getDataContratacao()
        );
    }
}
