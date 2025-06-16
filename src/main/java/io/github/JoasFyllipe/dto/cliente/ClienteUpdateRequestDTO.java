package io.github.JoasFyllipe.dto.cliente;

import java.time.LocalDate;
import java.util.List;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import jakarta.validation.constraints.NotBlank;


public record ClienteUpdateRequestDTO(

    @NotBlank(message = "O campo nome deve ser informado.")
    String nome,
    @NotBlank(message = "O campo cpf deve ser informado.")
    String cpf,
    @NotBlank(message = "O campo data de nascimento deve ser informado.")
    LocalDate dataNascimento,

    List<TelefoneRequestDTO> telefones,
    List<EnderecoRequestDTO> enderecos
) {}
