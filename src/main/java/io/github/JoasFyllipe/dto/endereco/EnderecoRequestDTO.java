package io.github.JoasFyllipe.dto.endereco;

import io.github.JoasFyllipe.model.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 100)
    String logradouro,

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10)
    String numero,

    @Size(max = 50, message = "Complemento deve ter no máximo 50 caracteres")
    String complemento,

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 50)
    String bairro,

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 50)
    String cidade,

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve conter 2 letras")
    String estado,

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 8, message = "CEP deve conter 8 dígitos")
    String cep
) {

    public static Endereco toEntity(EnderecoRequestDTO dto) {
        if (dto == null) return null;

        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
        return endereco;
    }
}
