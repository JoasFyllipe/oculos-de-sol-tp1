package io.github.JoasFyllipe.dto.telefone;

import io.github.JoasFyllipe.model.telefone.Telefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TelefoneResponseDTO(

    Long id,

    @NotBlank(message = "Número do telefone não pode estar em branco")
    @Size(max = 15, message = "Número do telefone não pode exceder 15 caracteres")
    String numero,

    @NotBlank(message = "Tipo de telefone não pode estar em branco")
    @Size(max = 20, message = "Tipo de telefone não pode exceder 20 caracteres")
    String tipo,

    @NotNull(message = "Campo 'principal' não pode ser nulo")
    Boolean principal) {

    public static TelefoneResponseDTO valueOf(Telefone telefone) {
        if (telefone == null) {
            return null;
        }
        return new TelefoneResponseDTO(
            telefone.getId(),
            telefone.getNumero(),
            telefone.getTipo(),
            telefone.isPrincipal()
        );
    }
}
