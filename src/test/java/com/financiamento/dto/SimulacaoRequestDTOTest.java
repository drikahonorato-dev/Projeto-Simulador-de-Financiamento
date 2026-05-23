package com.financiamento.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SimulacaoRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private SimulacaoRequestDTO requestValido() {
        SimulacaoRequestDTO req = new SimulacaoRequestDTO();
        req.valorInicial = new BigDecimal("1000.00");
        req.taxaJurosMensal = new BigDecimal("1.5");
        req.prazoMeses = 12;
        return req;
    }

    @Test
    void devePassarValidacaoComDadosValidos() {
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(requestValido());
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveRejeitarValorInicialNulo() {
        SimulacaoRequestDTO req = requestValido();
        req.valorInicial = null;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("valorInicial")));
    }

    @Test
    void deveRejeitarValorInicialZero() {
        SimulacaoRequestDTO req = requestValido();
        req.valorInicial = BigDecimal.ZERO;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("valorInicial")));
    }

    @Test
    void deveRejeitarValorInicialNegativo() {
        SimulacaoRequestDTO req = requestValido();
        req.valorInicial = new BigDecimal("-100.00");
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveRejeitarTaxaJurosMensalNula() {
        SimulacaoRequestDTO req = requestValido();
        req.taxaJurosMensal = null;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("taxaJurosMensal")));
    }

    @Test
    void deveRejeitarTaxaJurosMensalZero() {
        SimulacaoRequestDTO req = requestValido();
        req.taxaJurosMensal = BigDecimal.ZERO;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("taxaJurosMensal")));
    }

    @Test
    void deveRejeitarTaxaJurosMensalNegativa() {
        SimulacaoRequestDTO req = requestValido();
        req.taxaJurosMensal = new BigDecimal("-1.0");
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveRejeitarPrazoMesesNulo() {
        SimulacaoRequestDTO req = requestValido();
        req.prazoMeses = null;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("prazoMeses")));
    }

    @Test
    void deveRejeitarPrazoMesesZero() {
        SimulacaoRequestDTO req = requestValido();
        req.prazoMeses = 0;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("prazoMeses")));
    }

    @Test
    void deveRejeitarPrazoMesesNegativo() {
        SimulacaoRequestDTO req = requestValido();
        req.prazoMeses = -5;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    void deveAceitarValorInicialMinimo() {
        SimulacaoRequestDTO req = requestValido();
        req.valorInicial = new BigDecimal("0.01");
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveAceitarPrazoMesesUm() {
        SimulacaoRequestDTO req = requestValido();
        req.prazoMeses = 1;
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveRetornarMultiplasViolacoesParaTodosCamposNulos() {
        SimulacaoRequestDTO req = new SimulacaoRequestDTO();
        Set<ConstraintViolation<SimulacaoRequestDTO>> violations = validator.validate(req);
        assertEquals(3, violations.size());
    }
}
