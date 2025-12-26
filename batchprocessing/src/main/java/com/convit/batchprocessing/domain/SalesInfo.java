package com.convit.batchprocessing.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SalesInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long saleId;
    private Long productId;
    private Long customerId;
    private String saleDate;
    private Double saleAmount;
    private String location;
    private String country;
    private Boolean processed;
}