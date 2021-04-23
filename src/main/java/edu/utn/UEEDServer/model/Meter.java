package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "meters")
public class Meter {
    @Id
    @GeneratedValue
    private UUID serialNumber; // Set type UUID instead of String
    @Column(name="lastReading")
    private LocalDateTime lastMeasurementDate;
    @Column(name="accumulatedConsumption")
    private double accumulatedConsumption;
    @OneToOne
    private Brand brand;



}
