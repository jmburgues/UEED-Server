package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "METERS")
public class Meter {
    @Id
    private String serialNumber;

    @Column(name="lastReading",columnDefinition = "datetime default now()")
    private LocalDateTime lastReading;

    @Column(name="accumulatedConsumption",columnDefinition = "double default 0")
    private double accumulatedConsumption;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "meterSerialNumber",foreignKey = @ForeignKey(name="FK_meters_readings"))
    private List<Reading> readings;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="modelId",foreignKey = @ForeignKey(name="FK_meters_models"))
    private Model model;
    private String password;
}
    /*
    ManyToOne: Several meters can have the same model.
    optional false: this Object is mandatory for every meter.
    CascadeType: Test difference between ALL and PERSIST
     */
