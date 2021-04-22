package edu.utn.UEEDServer.model;

public class Address {

    private Integer id;
    private String street;
    private Integer number;
    private Rate rate;
    private Meter meter;

    public Address(String street, Integer number, Rate rate) {
        this.street = street;
        this.number = number;
        this.rate = rate;
        this.meter = null;
    }

    public Address(String street, Integer number, Rate rate, Meter meter) {
        this.street = street;
        this.number = number;
        this.rate = rate;
        this.meter = meter;
    }

    public Integer getId() {
        return id;
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

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Meter getMeter() {
        return meter;
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
    }

}
