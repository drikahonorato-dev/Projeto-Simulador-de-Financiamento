package com.financiamento.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parcelas_mensais")
public class ParcelaMensal extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simulacao_id", nullable = false)
    public Simulacao simulacao;

    @Column(nullable = false)
    public Integer mes;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal saldoInicial;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal juro;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal saldoFinal;
}
