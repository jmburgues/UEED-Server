package edu.utn.UEEDServer.model;
import java.time.LocalDateTime;

public class Reading {
    private Integer id;
    private LocalDateTime readDate;
    private float totalKw;
    private Meter meter;
    private Bill bill;

    public Reading() {
    }

    public Reading(Integer id, LocalDateTime readDate, float totalKw, Meter meter, Bill bill){
            this.id = id;
            this.readDate = readDate;
            this.totalKw = totalKw;
            this.meter = meter;
            this.bill = bill;
    }

    public Integer getId () {
        return id;
    }

    public void setId (Integer id){
        this.id = id;
    }

    public LocalDateTime getReadDate () {
        return readDate;
    }

    public void setReadDate (LocalDateTime readDate){
        this.readDate = readDate;
    }

    public float getTotalKw () {
        return totalKw;
    }

    public void setTotalKw ( float totalKw){
        this.totalKw = totalKw;
    }

    public Meter getMeter () {
        return meter;
    }

    public void setMeter (Meter meter){
        this.meter = meter;
    }

    public Bill getBill () {
        return bill;
    }

    public void setBill (Bill bill){
        this.bill = bill;
    }
}