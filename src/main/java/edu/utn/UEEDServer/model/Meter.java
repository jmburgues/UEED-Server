package edu.utn.UEEDServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private String serialNumber;

    @Column(name="lastReading",columnDefinition = "datetime default null")
    private LocalDateTime lastReading;

    @Column(name="accumulatedConsumption",columnDefinition = "double default 0")
    private double accumulatedConsumption;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "meterSerialNumber",foreignKey = @ForeignKey(name="FK_meters_readings"))
    @JsonIgnore
    private List<Reading> readings;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="modelId",foreignKey = @ForeignKey(name="FK_meters_models"))
    private Model model;
    private String password;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="addressId", columnDefinition = "int not null unique",foreignKey = @ForeignKey(name = "fk_METERS_addressId"))
    private Address address;
}
    /*
    ManyToOne: Several meters can have the same model.
    optional false: this Object is mandatory for every meter.
    CascadeType: Test difference between ALL and PERSIST
     */
