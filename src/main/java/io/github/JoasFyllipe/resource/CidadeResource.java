package io.github.JoasFyllipe.resource;

import io.github.JoasFyllipe.dto.endereco.CidadeResponseDTO;
import org.jboss.logging.Logger;

import io.github.JoasFyllipe.dto.endereco.CidadeRequestDTO;
import io.github.JoasFyllipe.service.endereco.CidadeService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CidadeResource {

    private static final Logger LOG = Logger.getLogger(CidadeResource.class);

    @Inject
    CidadeService service;

    @POST
    @RolesAllowed({"ADM", "EMPLOYE"}) // SECURITY: Apenas ADMIN pode criar
    public Response create(CidadeRequestDTO dto) {
        LOG.infof("Requisição para criar nova cidade: %s", dto.nome());
        CidadeResponseDTO response = service.create(dto);
        LOG.infof("Cidade criada com sucesso, id: %d", response.id());
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM", "EMPLOYE"}) // SECURITY: Apenas ADMIN pode atualizar
    public Response update(@PathParam("id") Long id, CidadeRequestDTO dto) {
        LOG.infof("Requisição para atualizar cidade de id: %d", id);
        CidadeResponseDTO response = service.update(id, dto);
        LOG.info("Cidade atualizada com sucesso.");
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"}) // SECURITY: Apenas ADMIN pode deletar
    public Response delete(@PathParam("id") Long id) {
        LOG.infof("Requisição para deletar cidade de id: %d", id);
        service.delete(id);
        LOG.info("Cidade deletada com sucesso.");
        return Response.noContent().build();
    }

    @GET
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem listar
    public Response findAll() {
        LOG.info("Requisição para listar todas as cidades.");
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem buscar por ID
    public Response findById(@PathParam("id") Long id) {
        LOG.infof("Requisição para buscar cidade por id: %d", id);
        return Response.ok(service.findById(id)).build();
    }

    @GET
    @Path("/estado/{estadoId}")
    @RolesAllowed({"ADM", "USER", "EMPLOYE"}) // SECURITY: ADMIN e USER podem buscar por estado
    public Response findByEstado(@PathParam("estadoId") Long estadoId) {
        LOG.infof("Requisição para buscar cidades do estado de id: %d", estadoId);
        return Response.ok(service.findByEstado(estadoId)).build();
    }
}