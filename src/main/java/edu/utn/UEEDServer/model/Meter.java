package edu.utn.UEEDServer.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Meter {
    @Id
    private UUID serialNumber; // Set type UUID instead of String
    private LocalDateTime lastMeasurementDate;
    private double accumulatedConsumption;
    private Brand brand;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meter meter = (Meter) o;
        return Objects.equals(serialNumber, meter.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }

    @Override
    public String toString() {
        return "Meter{" +
                "\nserialNumber='" + serialNumber + '\'' +
                "\n model=" + model +
                '}';
    }
}
