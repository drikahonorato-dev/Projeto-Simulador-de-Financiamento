package com.financiamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SimulacaoRequestDTO {

    @NotNull(message = "valorInicial é obrigatório")
    @DecimalMin(value = "0.01", message = "valorInicial deve ser maior que zero")
    public BigDecimal valorInicial;

    @NotNull(message = "taxaJurosMensal é obrigatória")
    @DecimalMin(value = "0.01", message = "taxaJurosMensal deve ser maior que zero")
    public BigDecimal taxaJurosMensal;

    @NotNull(message = "prazoMeses é obrigatório")
    @Min(value = 1, message = "prazoMeses deve ser pelo menos 1")
    public Integer prazoMeses;
}