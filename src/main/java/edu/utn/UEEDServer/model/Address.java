package edu.utn.UEEDServer.model;

public class Address {

    private Integer id;
    private String street;
    private Integer number;
    private Rate rate;
    private Client client;


    public Address(Integer id, Meter meter, String street, Integer number) {
        this.id = id;
        this.emeter = meter;
        this.street = street;
        this.number = number;
    }

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Meter getEmeter() {
        return emeter;
    }

    public void setEmeter(Meter meter) {
        this.emeter = meter;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
