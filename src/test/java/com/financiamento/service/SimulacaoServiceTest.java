package com.financiamento.service;

import com.financiamento.dto.SimulacaoRequestDTO;
import com.financiamento.dto.SimulacaoResponseDTO;
import com.financiamento.model.ParcelaMensal;
import com.financiamento.model.Simulacao;
import com.financiamento.repository.SimulacaoRepository;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SimulacaoServiceTest {

    @Mock
    SimulacaoRepository repository;

    @InjectMocks
    SimulacaoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCalcularJurosCompostosCorretamente() {
        doNothing().when(repository).persist(any(Simulacao.class));

        SimulacaoRequestDTO req = criarRequest("1000.00", "1.5", 2);
        SimulacaoResponseDTO resp = service.simular(req);

        assertEquals(new BigDecimal("1030.23"), resp.valorTotalFinal);
        assertEquals(new BigDecimal("30.23"), resp.valorTotalJuros);
        assertEquals(2, resp.memoriaCalculo.size());
    }

    @Test
    void deveCalcularJuros5PorCentoDoisMeses() {
        doNothing().when(repository).persist(any(Simulacao.class));

        SimulacaoRequestDTO req = criarRequest("1000.00", "5.0", 2);
        SimulacaoResponseDTO resp = service.simular(req);

        assertEquals(new BigDecimal("1102.50"), resp.valorTotalFinal);
        assertEquals(new BigDecimal("102.50"), resp.valorTotalJuros);
    }

    @Test
    void deveGerarMemoriaCalculoComMesesCorretos() {
        doNothing().when(repository).persist(any(Simulacao.class));

        SimulacaoRequestDTO req = criarRequest("500.00", "2.0", 3);
        SimulacaoResponseDTO resp = service.simular(req);

        assertEquals(3, resp.memoriaCalculo.size());
        assertEquals(1, resp.memoriaCalculo.get(0).mes);
        assertEquals(2, resp.memoriaCalculo.get(1).mes);
        assertEquals(3, resp.memoriaCalculo.get(2).mes);
    }

    @Test
    void deveTerSaldoFinalDoMesAnteriorIgualAoSaldoInicialDoProximoMes() {
        doNothing().when(repository).persist(any(Simulacao.class));

        SimulacaoRequestDTO req = criarRequest("1000.00", "2.0", 4);
        SimulacaoResponseDTO resp = service.simular(req);

        for (int i = 1; i < resp.memoriaCalculo.size(); i++) {
            BigDecimal saldoFinalAnterior = resp.memoriaCalculo.get(i - 1).saldoFinal;
            BigDecimal saldoInicialAtual = resp.memoriaCalculo.get(i).saldoInicial;
            assertEquals(saldoFinalAnterior, saldoInicialAtual);
        }
    }

    @Test
    void devePersistirUmaVez() {
        doNothing().when(repository).persist(any(Simulacao.class));

        service.simular(criarRequest("1000.00", "1.0", 6));

        verify(repository, times(1)).persist(any(Simulacao.class));
    }

    @Test
    void deveRetornarIdDaSimulacaoAposSimular() {
        doAnswer(invocation -> {
            Simulacao s = invocation.getArgument(0);
            s.id = 42L;
            return null;
        }).when(repository).persist(any(Simulacao.class));

        SimulacaoResponseDTO resp = service.simular(criarRequest("1000.00", "1.5", 1));

        assertEquals(42L, resp.id);
    }

    @Test
    void devePreservarCamposDeEntradaNaResposta() {
        doNothing().when(repository).persist(any(Simulacao.class));

        SimulacaoResponseDTO resp = service.simular(criarRequest("2500.00", "3.0", 6));

        assertEquals(0, new BigDecimal("2500.00").compareTo(resp.valorInicial));
        assertEquals(0, new BigDecimal("3.0").compareTo(resp.taxaJurosMensal));
        assertEquals(6, resp.prazoMeses);
    }

    @Test
    void deveCalcularJurosParaPrazoUmMes() {
        doNothing().when(repository).persist(any(Simulacao.class));

        SimulacaoResponseDTO resp = service.simular(criarRequest("1000.00", "10.0", 1));

        assertEquals(new BigDecimal("1100.00"), resp.valorTotalFinal);
        assertEquals(new BigDecimal("100.00"), resp.valorTotalJuros);
        assertEquals(1, resp.memoriaCalculo.size());
        assertEquals(new BigDecimal("1000.00"), resp.memoriaCalculo.get(0).saldoInicial);
        assertEquals(new BigDecimal("100.00"), resp.memoriaCalculo.get(0).juro);
        assertEquals(new BigDecimal("1100.00"), resp.memoriaCalculo.get(0).saldoFinal);
    }

    @Test
    void deveBuscarSimulacaoExistente() {
        Simulacao simulacao = criarSimulacaoMock(7L);
        when(repository.findByIdOptional(7L)).thenReturn(Optional.of(simulacao));

        SimulacaoResponseDTO resp = service.buscarPorId(7L);

        assertNotNull(resp);
        assertEquals(7L, resp.id);
    }

    @Test
    void deveLancarNotFoundQuandoSimulacaoNaoExiste() {
        when(repository.findByIdOptional(99L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.buscarPorId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void deveLancarNotFoundParaIdZero() {
        when(repository.findByIdOptional(0L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(0L));
    }

    @Test
    void deveBuscarERetornarMemoriaCalculo() {
        Simulacao simulacao = criarSimulacaoMock(5L);

        ParcelaMensal p = new ParcelaMensal();
        p.mes = 1;
        p.saldoInicial = new BigDecimal("1000.00");
        p.juro = new BigDecimal("15.00");
        p.saldoFinal = new BigDecimal("1015.00");
        p.simulacao = simulacao;
        simulacao.memoriaCalculo.add(p);

        when(repository.findByIdOptional(5L)).thenReturn(Optional.of(simulacao));

        SimulacaoResponseDTO resp = service.buscarPorId(5L);

        assertEquals(1, resp.memoriaCalculo.size());
        assertEquals(1, resp.memoriaCalculo.get(0).mes);
        assertEquals(new BigDecimal("1015.00"), resp.memoriaCalculo.get(0).saldoFinal);
    }

    private SimulacaoRequestDTO criarRequest(String valor, String taxa, int prazo) {
        SimulacaoRequestDTO req = new SimulacaoRequestDTO();
        req.valorInicial = new BigDecimal(valor);
        req.taxaJurosMensal = new BigDecimal(taxa);
        req.prazoMeses = prazo;
        return req;
    }

    private Simulacao criarSimulacaoMock(Long id) {
        Simulacao s = new Simulacao();
        s.id = id;
        s.valorInicial = new BigDecimal("1000.00");
        s.taxaJurosMensal = new BigDecimal("1.5000");
        s.prazoMeses = 12;
        s.valorTotalFinal = new BigDecimal("1195.62");
        s.valorTotalJuros = new BigDecimal("195.62");
        return s;
    }
}