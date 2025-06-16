package io.github.JoasFyllipe.dto.endereco;

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
    Long idCidade,

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 8, message = "CEP deve conter 8 dígitos")
    String cep
) {
}
