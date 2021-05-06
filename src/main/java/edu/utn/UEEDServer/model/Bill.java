package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BILLS")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="billId")
    private Integer billId;
    @Column(name="billedDate")
    private LocalDateTime billedDate; ///how do we convert this into java.sql.date to store in DB?
    @Column(name="initialConsumption")
    private Integer initialConsumption;
    @Column(name="finalConsumption")
    private Integer finalConsumption;
    @Column(name="totalConsumption")
    private Integer totalConsumption;
    @Column(name="meterId")
    private String meterId; // just for info, not for linking to Meter object.

    private RateCategory rateCategory;
    @Column(name="ratePrice")
    private float ratePrice;
    @Column(name="totalPrice")
    private float totalPrice;
    @Column(name="clientId")
    private Integer clientId; // just for info, not for linking to Client object.

    // is this model ok??


    }
