package io.github.JoasFyllipe.dto.endereco;

import io.github.JoasFyllipe.model.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoResponseDTO(
    Long id,

    @NotBlank(message = "Logradouro não pode estar em branco")
    @Size(max = 100, message = "Logradouro deve ter no máximo 100 caracteres")
    String logradouro,

    @NotBlank(message = "Número não pode estar em branco")
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    String numero,

    @Size(max = 50, message = "Complemento deve ter no máximo 50 caracteres")
    String complemento,

    @NotBlank(message = "Bairro não pode estar em branco")
    @Size(max = 50, message = "Bairro deve ter no máximo 50 caracteres")
    String bairro,

    @NotBlank(message = "Cidade não pode estar em branco")
    @Size(max = 50, message = "Cidade deve ter no máximo 50 caracteres")
    String cidade,

    @Size(min = 2, max = 2, message = "Estado deve conter exatamente 2 letras")
    String estado,

    @Size(min = 8, max = 8, message = "CEP deve conter exatamente 8 dígitos")
    String cep
) {

    public static EnderecoResponseDTO valueOf(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoResponseDTO(
            endereco.getId(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }

    public EnderecoResponseDTO() {
        this(null, null, null, null, null, null, null, null);
    }

    public EnderecoResponseDTO(Long id, String logradouro, String numero, String cidade) {
        this(id, logradouro, numero, null, null, cidade, null, null);
    }

    public EnderecoResponseDTO(
        Long id, String logradouro, String numero, String complemento,
        String bairro, String cidade, String estado, String cep
    ) {
        this.id = id;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
}
