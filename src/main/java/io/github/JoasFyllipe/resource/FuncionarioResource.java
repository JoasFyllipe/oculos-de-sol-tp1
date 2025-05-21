package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.github.JoasFyllipe.service.funcionario.FuncionarioService;
import java.util.List;

@Path("/funcionarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioResource {

    @Inject
    FuncionarioService funcionarioService;

    // Método para buscar todos os funcionários
    @GET
    public List<FuncionarioResponseDTO> buscarTodos() {
        return funcionarioService.findAll();
    }

    // Método para buscar um funcionário por ID
    @GET
    @Path("/{id}")
    public FuncionarioResponseDTO buscarPorId(@PathParam("id") Long id) {
        return funcionarioService.findById(id);
    }

    // Método para adicionar um novo funcionário
    @POST
    public FuncionarioResponseDTO adicionarFuncionario(FuncionarioRequestDTO funcionarioDTO) {
        return funcionarioService.create(funcionarioDTO);
    }

    // Método para atualizar as informações de um funcionário
    @PUT
    @Path("{id}")
    @Transactional
    public Response atualizarFuncionario(@PathParam("id") Long id, FuncionarioUpdateRequestDTO funcionarioDTO) {
        try {
            funcionarioService.update(id, funcionarioDTO);  // A execução do update
            return Response.noContent().build();  // Retorna status 204 (No Content) se atualizado com sucesso
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Funcionário não encontrado para atualização").build();  // Retorna 404 caso não encontre
        }
    }

    // Método para deletar um funcionário por ID
    @DELETE
    @Path("{id}")
    public Response deletarFuncionario(@PathParam("id") Long id) {
        try {
            funcionarioService.delete(id);  // A execução do delete
            return Response.noContent().build();  // Retorna status 204 (No Content) se deletado com sucesso
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Funcionário não encontrado para exclusão").build();  // Retorna 404 caso não encontre
        }
    }
}
