package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID serialNumber;

    @Column(columnDefinition = "datetime default now()")
    private LocalDateTime lastReading;

    @Column(columnDefinition = "double default 0")
    private double accumulatedConsumption;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "meterSerialNumber")
    private List<Reading> readings;

    /*
    ManyToOne: Several meters can have the same model.
    optional false: this Object is mandatory for every meter.
    CascadeType: Test difference between ALL and PERSIST
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="modelId")
    private Model model;
}
