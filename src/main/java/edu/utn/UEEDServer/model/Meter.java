package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "meterSerialNumber")
    private List<Reading> readings;

    @OneToOne
    private Brand brand;



}
