package io.github.JoasFyllipe.dto.cliente;

import java.time.LocalDateTime;

import io.github.JoasFyllipe.dto.usuario.UsuarioResponseDTO;
import io.github.JoasFyllipe.model.usuario.Cliente;

public record ClienteResponseDTO(

    Long id,
    UsuarioResponseDTO usuarioResponseDTO,
    LocalDateTime dataCadastro

) {

    public static ClienteResponseDTO valueOf(Cliente cliente){
        if(cliente == null)
            return null;

        return new ClienteResponseDTO(
            cliente.getId(),
            UsuarioResponseDTO.valueOf(cliente.getUsuario()),
            cliente.getDataCadastro()
        );
        
    }

}