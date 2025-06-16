package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.endereco.EstadoRequestDTO;
import org.jboss.logging.Logger;

import io.github.JoasFyllipe.dto.endereco.EstadoResponseDTO;
import io.github.JoasFyllipe.service.endereco.EstadoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/estados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoResource {

    private static final Logger LOG = Logger.getLogger(EstadoResource.class);

    @Inject
    public EstadoService service;

    @POST
    @RolesAllowed({"ADM"}) // SECURITY: Apenas ADMIN pode criar
    public Response create(EstadoRequestDTO dto) {
        // LOG: Logando a requisição de criação
        LOG.infof("Requisição para criar novo estado: %s", dto.nome());
        EstadoResponseDTO response = service.create(dto);
        LOG.infof("Estado criado com sucesso, id: %d", response.id());
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM"}) // SECURITY: Apenas ADMIN pode atualizar
    public Response update(@PathParam("id") Long id, EstadoRequestDTO dto) {
        LOG.infof("Requisição para atualizar estado de id: %d", id);
        EstadoResponseDTO response = service.update(id, dto);
        LOG.info("Estado atualizado com sucesso.");
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"}) // SECURITY: Apenas ADMIN pode deletar
    public Response delete(@PathParam("id") Long id) {
        LOG.infof("Requisição para deletar estado de id: %d", id);
        service.delete(id);
        LOG.info("Estado deletado com sucesso.");
        return Response.noContent().build();
    }

    @GET
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem listar
    public Response findAll() {
        LOG.info("Requisição para listar todos os estados.");
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem buscar por ID
    public Response findById(@PathParam("id") Long id) {
        LOG.infof("Requisição para buscar estado por id: %d", id);
        return Response.ok(service.findById(id)).build();
    }

    @GET
    @Path("/busca/sigla/{sigla}")
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem buscar por sigla
    public Response findBySigla(@PathParam("sigla") String sigla) {
        LOG.infof("Requisição para buscar estado por sigla: %s", sigla);
        return Response.ok(service.findBySigla(sigla)).build();
    }

    @GET
    @Path("/busca/nome/{nome}")
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem buscar por nome
    public Response findByNomeLike(@PathParam("nome") String nome) {
        LOG.infof("Requisição para buscar estado por nome contendo: %s", nome);
        return Response.ok(service.findByNomeLike(nome)).build();
    }
}