package com.financiamento.dto;

import java.math.BigDecimal;
import java.util.List;

public class SimulacaoResponseDTO {
    public Long id;
    public BigDecimal valorInicial;
    public BigDecimal taxaJurosMensal;
    public Integer prazoMeses;
    public BigDecimal valorTotalFinal;
    public BigDecimal valorTotalJuros;
    public List<ParcelaMensalDTO> memoriaCalculo;
}
