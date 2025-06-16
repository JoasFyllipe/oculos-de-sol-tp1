package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioRequestDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioResponseDTO;
import io.github.JoasFyllipe.dto.funcionario.FuncionarioUpdateRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.service.usuario.FuncionarioService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/funcionarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioResource {

    @Inject
    FuncionarioService funcionarioService;

    @Inject
    JsonWebToken jwt;

    private static final Logger LOG = Logger.getLogger(FuncionarioResource.class);

    private String getUsuarioEmail() {
        String email = jwt.getClaim("upn");
        if (email == null) {
            throw new WebApplicationException("Token inválido ou sem o claim 'upn'.", Response.Status.FORBIDDEN);
        }
        return email;
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response adicionarFuncionario(@Valid FuncionarioRequestDTO funcionarioDTO) {
        LOG.info("Admin criando novo funcionário.");
        FuncionarioResponseDTO funcionario = funcionarioService.create(funcionarioDTO);
        return Response.status(Response.Status.CREATED).entity(funcionario).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response atualizarFuncionario(@PathParam("id") Long id, @Valid FuncionarioUpdateRequestDTO funcionarioDTO) {
        LOG.infof("Admin atualizando funcionário com ID: %d", id);
        funcionarioService.update(id, funcionarioDTO);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response deletarFuncionario(@PathParam("id") Long id) {
        LOG.infof("Admin deletando funcionário com ID: %d", id);
        funcionarioService.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public FuncionarioResponseDTO buscarPorId(@PathParam("id") Long id) {
        LOG.infof("Admin buscando funcionário por ID: %d", id);
        return funcionarioService.findById(id);
    }

    @GET
    @Path("/search/nome/{nome}")
    @RolesAllowed("ADMIN")
    public FuncionarioResponseDTO buscarPorNome(@PathParam("nome") String nome) {
        LOG.infof("Admin buscando funcionário por nome: %s", nome);
        return funcionarioService.findByNome(nome);
    }

    @GET
    @RolesAllowed("ADMIN")
    public List<FuncionarioResponseDTO> buscarTodos() {
        LOG.info("Admin acessando lista de todos os funcionários.");
        return funcionarioService.findAll();
    }

    @GET
    @Path("/search/email/{email}")
    @RolesAllowed("ADMIN")
    public FuncionarioResponseDTO buscarPorEmail(@PathParam("email") String email) {
        LOG.infof("Admin buscando funcionário por e-mail: %s", email);
        return funcionarioService.findByEmail(email);
    }

    @GET
    @Path("/search/cargo/{cargo}")
    @RolesAllowed("ADMIN")
    public List<FuncionarioResponseDTO> buscarPorCargo(@PathParam("cargo") String cargo) {
        LOG.infof("Admin buscando funcionários por cargo: %s", cargo);
        return funcionarioService.findByCargo(cargo);
    }

    @PUT
    @Path("/me/enderecos")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateEnderecos(@Valid List<EnderecoRequestDTO> enderecos) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria lista de endereços.", email);
        funcionarioService.updateEndereco(email, enderecos);
        return Response.noContent().build();
    }

    @PUT
    @Path("/me/telefones")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateTelefones(@Valid List<TelefoneRequestDTO> telefones) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria lista de telefones.", email);
        funcionarioService.updateTelefone(email, telefones);
        return Response.noContent().build();
    }

    @PUT
    @Path("/me/telefones/{idTelefone}")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateTelefoneEspecifico(@PathParam("idTelefone") Long idTelefone, @Valid TelefoneRequestDTO telefoneDTO) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o telefone específico de ID %d.", email, idTelefone);
        funcionarioService.updateTelefoneEspecifico(email, idTelefone, telefoneDTO);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/senha")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateSenha(@Valid SenhaPatchRequestDTO senhaPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria senha.", email);
        funcionarioService.updateSenha(email, senhaPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/cpf")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateCpf(@Valid CpfPatchRequestDTO cpfPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o próprio CPF.", email);
        funcionarioService.updateCpf(email, cpfPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/nome")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateNome(@Valid NomePatchRequestDTO nomePatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o próprio nome.", email);
        funcionarioService.updateNome(email, nomePatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/email")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateEmail(@Valid EmailPatchRequestDTO emailPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o próprio e-mail.", email);
        funcionarioService.updateEmail(email, emailPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/data-nascimento")
    @RolesAllowed({"EMPLOYE", "ADMIN"})
    public Response updateDataNascimento(@Valid DataNascimentoPatchDTO dataNascimentoPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria data de nascimento.", email);
        funcionarioService.updateDataNascimento(email, dataNascimentoPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/salario")
    @RolesAllowed({"ADMIN"}) // Apenas ADMIN pode alterar salário, mesmo o próprio
    public Response updateSalario(@Valid SalarioPatchRequestDTO salarioPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Admin %s atualizando o próprio salário.", email);
        funcionarioService.updateSalario(email, salarioPatch);
        return Response.noContent().build();
    }
}