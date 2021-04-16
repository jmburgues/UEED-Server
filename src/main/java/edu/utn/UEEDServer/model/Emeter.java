package edu.utn.UEEDServer.model;
import java.util.Objects;

public class Emeter {


    private String serialNumber;
    private Model model;


    public Emeter(String serialNumber,Model model)
    {
        this.serialNumber = serialNumber;
        this.model = model;

    }

    public Emeter(String serialNumber) {

        this.model = new Model();
        this.serialNumber = serialNumber;


    }

    public Emeter() {

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
        Emeter emeter = (Emeter) o;
        return Objects.equals(serialNumber, emeter.serialNumber);
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
