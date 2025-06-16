package io.github.JoasFyllipe.resource;

import java.net.URI;
import java.util.List;

import io.github.JoasFyllipe.dto.cliente.ClienteRequestDTO;
import io.github.JoasFyllipe.dto.cliente.ClienteResponseDTO;
import io.github.JoasFyllipe.dto.endereco.EnderecoRequestDTO;
import io.github.JoasFyllipe.dto.telefone.TelefoneRequestDTO;
import io.github.JoasFyllipe.dto.usuario.patch.*;
import io.github.JoasFyllipe.service.usuario.ClienteService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {

    private static final Logger LOG = Logger.getLogger(ClienteResource.class);

    @Inject
    ClienteService clienteService;

    @Inject
    JsonWebToken jwt;

    private String getUsuarioEmail() {
        String email = jwt.getClaim("upn");
        if (email == null) {
            throw new WebApplicationException("Token inválido ou sem o claim 'upn'.", Response.Status.FORBIDDEN);
        }
        return email;
    }

    @POST
    @PermitAll
    public Response create(@Valid ClienteRequestDTO clienteDTO) {
        LOG.info("Recebida nova solicitação de cadastro de cliente.");
        ClienteResponseDTO cliente = clienteService.create(clienteDTO);
        return Response.status(Response.Status.CREATED).entity(cliente).build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response getMinhasInformacoes() {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s buscando suas próprias informações de cliente.", email);
        return Response.ok(clienteService.getMinhasInformacoes(email)).build();
    }

    @PUT
    @Path("/me/enderecos")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMeusEnderecos(List<EnderecoRequestDTO> enderecos) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria lista de endereços.", email);
        clienteService.updateEndereco(email, enderecos);
        return Response.noContent().build();
    }

    @PUT
    @Path("/me/telefones")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMeusTelefones(List<TelefoneRequestDTO> telefones) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria lista de telefones.", email);
        clienteService.updateTelefone(email, telefones);
        return Response.noContent().build();
    }

    @PUT
    @Path("/me/telefones/{idTelefone}")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMeuTelefoneEspecifico(@PathParam("idTelefone") Long idTelefone, TelefoneRequestDTO telefoneDTO) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o telefone específico de ID %d.", email, idTelefone);
        clienteService.updateTelefoneEspecifico(email, idTelefone, telefoneDTO);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/senha")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMinhaSenha(SenhaPatchRequestDTO senhaPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria senha.", email);
        clienteService.updateSenha(email, senhaPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/cpf")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMeuCpf(CpfPatchRequestDTO cpfPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o próprio CPF.", email);
        clienteService.updateCpf(email, cpfPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/nome")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMeuNome(NomePatchRequestDTO nomePatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o próprio nome.", email);
        clienteService.updateNome(email, nomePatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/email")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMeuEmail(EmailPatchRequestDTO emailPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando o próprio e-mail.", email);
        clienteService.updateEmail(email, emailPatch);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/me/data-nascimento")
    @RolesAllowed({"USER", "ADM", "EMPLOYE"})
    public Response updateMinhaDataNascimento(DataNascimentoPatchDTO dataNascimentoPatch) {
        String email = getUsuarioEmail();
        LOG.infof("Usuário %s atualizando a própria data de nascimento.", email);
        clienteService.updateDataNascimento(email, dataNascimentoPatch);
        return Response.noContent().build();
    }

    // --- ENDPOINTS DE ADMINISTRAÇÃO ---
    // Apenas usuários com o perfil "ADMIN" podem acessar estes métodos.

    @GET
    @RolesAllowed({"ADM", "EMPLOYE"})
    public Response findAll(){
        LOG.info("Admin executando findAll de clientes.");
        return Response.ok(clienteService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "EMPLOYE"})
    public Response findById(@PathParam("id") Long id){
        LOG.infof("Admin buscando cliente por ID: %d", id);
        return Response.ok(clienteService.findById(id)).build();
    }

    @GET
    @Path("/search/nome/{nome}")
    @RolesAllowed({"ADM", "EMPLOYE"})
    public Response findByNome(@PathParam("nome") String nome){
        LOG.infof("Admin buscando cliente por nome: %s", nome);
        return Response.ok(clienteService.findByNome(nome)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response delete(@PathParam("id") Long id){
        LOG.infof("Admin deletando cliente com ID: %d", id);
        clienteService.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/from-funcionario/{email}")
    @RolesAllowed({"ADM"})
    public Response clienteFromFuncionario(@PathParam("email") String email){
        LOG.infof("Admin gerando cliente a partir do funcionário com e-mail: %s", email);
        return Response.ok(clienteService.clienteFromFuncionario(email)).build();
    }
}