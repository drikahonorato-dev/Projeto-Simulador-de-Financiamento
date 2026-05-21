package com.financiamento.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulacoes")
public class Simulacao extends PanacheEntity {

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorInicial;

    @Column(nullable = false, precision = 10, scale = 4)
    public BigDecimal taxaJurosMensal;

    @Column(nullable = false)
    public Integer prazoMeses;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorTotalFinal;

    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal valorTotalJuros;

    @OneToMany(mappedBy = "simulacao", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    public List<ParcelaMensal> memoriaCalculo = new ArrayList<>();
}