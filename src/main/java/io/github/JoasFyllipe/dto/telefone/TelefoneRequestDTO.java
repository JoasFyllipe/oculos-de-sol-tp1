package io.github.JoasFyllipe.dto.telefone;

import io.github.JoasFyllipe.model.usuario.telefone.Telefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TelefoneRequestDTO(

        @NotBlank(message = "O DDD não pode estar em branco")
        @Size(min = 2, max = 2, message = "O DDD deve ter exatamente 2 dígitos")
        //Garante que apenas números sejam inseridos.
        @Pattern(regexp = "^[0-9]{2}$", message = "O DDD deve conter apenas números")
        String ddd,

        @NotBlank(message = "O número do telefone não pode estar em branco")
        @Size(min = 8, max = 9, message = "O número do telefone deve ter entre 8 e 9 dígitos")
        // Garante que o número tenha 8 ou 9 dígitos e apenas números.
        @Pattern(regexp = "^[0-9]{8,9}$", message = "O número do telefone deve conter apenas números")
        String numero,

        @NotNull(message = "O campo 'principal' não pode ser nulo")
        Boolean principal,

        @NotNull(message = "O id do tipo do telefone não pode ser nulo")
        Integer idTipoTelefone
) {

    public static Telefone toEntity(TelefoneRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Telefone telefone = new Telefone();
        telefone.setDdd(dto.ddd());
        telefone.setNumero(dto.numero());
        telefone.setPrincipal(dto.principal());
        return telefone;
    }
}