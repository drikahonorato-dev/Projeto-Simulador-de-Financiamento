package com.financiamento.resource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationExceptionMapperTest {

    private ValidationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ValidationExceptionMapper();
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> extrairErrors(Response response) {
        Map<String, Object> body = (Map<String, Object>) response.getEntity();
        return (Map<String, String>) body.get("errors");
    }

    @Test
    void deveRetornarStatus400() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(path.toString()).thenReturn("valorInicial");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("obrigatorio");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        Response response = mapper.toResponse(ex);
        assertEquals(400, response.getStatus());
    }

    @Test
    void deveRetornarBodyComCampoErrors() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(path.toString()).thenReturn("taxaJurosMensal");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("obrigatoria");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        Response response = mapper.toResponse(ex);
        Map<String, Object> body = (Map<String, Object>) response.getEntity();
        assertTrue(body.containsKey("errors"));
    }

    @Test
    void deveMapearCampoEMensagemCorretamente() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);
        when(path.toString()).thenReturn("prazoMeses");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("deve ser pelo menos 1");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        Map<String, String> errors = extrairErrors(mapper.toResponse(ex));
        assertEquals("deve ser pelo menos 1", errors.get("prazoMeses"));
    }

    @Test
    void deveMapearMultiplasViolacoes() {
        ConstraintViolation<?> v1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> v2 = mock(ConstraintViolation.class);
        jakarta.validation.Path p1 = mock(jakarta.validation.Path.class);
        jakarta.validation.Path p2 = mock(jakarta.validation.Path.class);
        when(p1.toString()).thenReturn("valorInicial");
        when(v1.getPropertyPath()).thenReturn(p1);
        when(v1.getMessage()).thenReturn("obrigatorio");
        when(p2.toString()).thenReturn("prazoMeses");
        when(v2.getPropertyPath()).thenReturn(p2);
        when(v2.getMessage()).thenReturn("deve ser pelo menos 1");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(v1, v2));
        Map<String, String> errors = extrairErrors(mapper.toResponse(ex));
        assertEquals(2, errors.size());
        assertTrue(errors.containsKey("valorInicial"));
        assertTrue(errors.containsKey("prazoMeses"));
    }
}