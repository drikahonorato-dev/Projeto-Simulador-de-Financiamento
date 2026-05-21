package com.financiamento.service;

import com.financiamento.dto.*;
import com.financiamento.model.*;
import com.financiamento.repository.SimulacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    @Inject
    SimulacaoRepository repository;

    @Transactional
    public SimulacaoResponseDTO simular(SimulacaoRequestDTO request) {
        BigDecimal taxa = request.taxaJurosMensal
                .divide(BigDecimal.valueOf(100), MC);

        BigDecimal saldo = request.valorInicial;
        List<ParcelaMensal> parcelas = new ArrayList<>();

        for (int mes = 1; mes <= request.prazoMeses; mes++) {
            BigDecimal saldoInicial = saldo.setScale(2, RoundingMode.HALF_UP);
            BigDecimal juro = saldoInicial.multiply(taxa, MC).setScale(2, RoundingMode.HALF_UP);
            BigDecimal saldoFinal = saldoInicial.add(juro).setScale(2, RoundingMode.HALF_UP);

            ParcelaMensal parcela = new ParcelaMensal();
            parcela.mes = mes;
            parcela.saldoInicial = saldoInicial;
            parcela.juro = juro;
            parcela.saldoFinal = saldoFinal;

            parcelas.add(parcela);
            saldo = saldoFinal;
        }

        BigDecimal valorTotalFinal = saldo.setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotalJuros = valorTotalFinal
                .subtract(request.valorInicial)
                .setScale(2, RoundingMode.HALF_UP);

        Simulacao simulacao = new Simulacao();
        simulacao.valorInicial = request.valorInicial;
        simulacao.taxaJurosMensal = request.taxaJurosMensal;
        simulacao.prazoMeses = request.prazoMeses;
        simulacao.valorTotalFinal = valorTotalFinal;
        simulacao.valorTotalJuros = valorTotalJuros;
        simulacao.memoriaCalculo = parcelas;

        for (ParcelaMensal p : parcelas) {
            p.simulacao = simulacao;
        }

        repository.persist(simulacao);

        return toResponse(simulacao);
    }

    public SimulacaoResponseDTO buscarPorId(Long id) {
        Simulacao simulacao = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Simulação não encontrada com ID: " + id));
        return toResponse(simulacao);
    }

    private SimulacaoResponseDTO toResponse(Simulacao simulacao) {
        SimulacaoResponseDTO response = new SimulacaoResponseDTO();
        response.id = simulacao.id;
        response.valorInicial = simulacao.valorInicial;
        response.taxaJurosMensal = simulacao.taxaJurosMensal;
        response.prazoMeses = simulacao.prazoMeses;
        response.valorTotalFinal = simulacao.valorTotalFinal;
        response.valorTotalJuros = simulacao.valorTotalJuros;
        response.memoriaCalculo = simulacao.memoriaCalculo.stream()
                .map(p -> new ParcelaMensalDTO(p.mes, p.saldoInicial, p.juro, p.saldoFinal))
                .toList();
        return response;
    }
}