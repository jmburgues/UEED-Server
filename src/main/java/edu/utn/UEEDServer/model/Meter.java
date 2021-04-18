package edu.utn.UEEDServer.model;
import java.time.LocalDateTime;
import java.util.Objects;

public class Meter {
    private String serialNumber; // Set type UUID instead of String
    private LocalDateTime lastMeasurementDate;
    private double accumulatedConsumption;
    private Model model;

    public Meter(String serialNumber, Model model) {
        this.serialNumber = serialNumber;
        this.lastMeasurementDate = LocalDateTime.now();
        this.accumulatedConsumption = 0;
        this.model = model;
    }

    public Meter(String serialNumber) {
        this.model = new Model();
        this.lastMeasurementDate = LocalDateTime.now();
        this.accumulatedConsumption = 0;
        this.serialNumber = serialNumber;
    }

    public Meter() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

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
        return "Emeter{" +
                "\nserialNumber='" + serialNumber + '\'' +
                "\n model=" + model +
                '}';
    }
}
