package edu.utn.UEEDServer.model;

public class Address {

    private Integer id;
    private Emeter emeter;
    private Rate rate;
    private String street;
    private Integer number;

    public Address(Integer id, Emeter emeter, String street, Integer number) {
        this.id = id;
        this.emeter = emeter;
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

    public Emeter getEmeter() {
        return emeter;
    }

    public void setEmeter(Emeter emeter) {
        this.emeter = emeter;
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
