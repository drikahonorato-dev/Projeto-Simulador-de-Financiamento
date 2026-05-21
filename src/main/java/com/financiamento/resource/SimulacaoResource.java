package com.financiamento.resource;

import com.financiamento.dto.SimulacaoRequestDTO;
import com.financiamento.dto.SimulacaoResponseDTO;
import com.financiamento.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulações", description = "API de simulação de financiamentos")
public class SimulacaoResource {

    @Inject
    SimulacaoService service;

    @POST
    @Operation(summary = "Criar nova simulação de financiamento")
    public Response simular(@Valid SimulacaoRequestDTO request) {
        SimulacaoResponseDTO response = service.simular(request);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Consultar simulação por ID")
    public Response buscarPorId(@PathParam("id") Long id) {
        SimulacaoResponseDTO response = service.buscarPorId(id);
        return Response.ok(response).build();
    }
}