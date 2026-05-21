package com.financiamento.dto;

import java.math.BigDecimal;

public class ParcelaMensalDTO {
    public Integer mes;
    public BigDecimal saldoInicial;
    public BigDecimal juro;
    public BigDecimal saldoFinal;

    public ParcelaMensalDTO(Integer mes, BigDecimal saldoInicial, BigDecimal juro, BigDecimal saldoFinal) {
        this.mes = mes;
        this.saldoInicial = saldoInicial;
        this.juro = juro;
        this.saldoFinal = saldoFinal;
    }
}