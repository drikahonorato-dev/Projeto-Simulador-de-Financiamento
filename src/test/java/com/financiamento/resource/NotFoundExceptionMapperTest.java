package com.financiamento.resource;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionMapperTest {

    private NotFoundExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotFoundExceptionMapper();
    }

    @Test
    void deveRetornarStatus404() {
        NotFoundException ex = new NotFoundException("Recurso nao encontrado");
        Response response = mapper.toResponse(ex);
        assertEquals(404, response.getStatus());
    }

    @Test
    void deveRetornarBodyComCampoError() {
        NotFoundException ex = new NotFoundException("Simulacao nao encontrada com ID: 99");
        Response response = mapper.toResponse(ex);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getEntity();
        assertTrue(body.containsKey("error"));
    }

    @Test
    void deveRetornarMensagemCorreta() {
        String mensagem = "Simulacao nao encontrada com ID: 42";
        NotFoundException ex = new NotFoundException(mensagem);
        Response response = mapper.toResponse(ex);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getEntity();
        assertEquals(mensagem, body.get("error"));
    }

    @Test
    void deveRetornarStatus404ParaMensagemVazia() {
        NotFoundException ex = new NotFoundException("");
        Response response = mapper.toResponse(ex);
        assertEquals(404, response.getStatus());
    }
}
