package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "BILLS")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;
    private String description;
    private LocalDateTime billedDate; ///how do we convert this into java.sql.date to store in DB?
    private LocalDateTime initialReadingDate;
    private LocalDateTime finalReadingDate;
    private Float initialConsumption;
    private Float finalConsumption;
    private Float totalConsumption;
    private String meterId; // just for info, not for linking to Meter object.
    private String rateCategory;
    private float ratePrice;
    private Integer clientId; // just for info, not for linking to Client object.
    private Boolean paid;

    // is this model ok??


    }
