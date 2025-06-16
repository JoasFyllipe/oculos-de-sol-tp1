package io.github.JoasFyllipe.dto.endereco;

import io.github.JoasFyllipe.model.usuario.endereco.EnderecoEntrega;
import io.github.JoasFyllipe.model.usuario.endereco.Endereco;

public record EnderecoResponseDTO(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        CidadeResponseDTO cidade,
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
                CidadeResponseDTO.valueOf(endereco.getCidade()),
                endereco.getCep()
        );
    }

    public static EnderecoResponseDTO valueOf(EnderecoEntrega endereco) {
        if (endereco == null) {
            return null;
        }

        // Criamos um CidadeResponseDTO a partir dos dados da "fotografia".
        // Os IDs são nulos porque não temos a referência direta, apenas os nomes/siglas.
        CidadeResponseDTO cidade = new CidadeResponseDTO(
                null,                      // id da cidade
                endereco.getCidadeNome(),  // nome da cidade
                null,                      // id do estado
                endereco.getEstadoNome(),  // nome do estado (do campo que recomendamos adicionar)
                endereco.getEstadoSigla()  // sigla do estado
        );

        return new EnderecoResponseDTO(
                endereco.getId(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                cidade,
                endereco.getCep()
        );
    }
}