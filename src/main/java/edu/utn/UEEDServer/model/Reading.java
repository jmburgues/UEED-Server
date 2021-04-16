package edu.utn.UEEDServer.model;
import java.util.Date;


public class Reading {

    private static Date INITIAL_DATE = new Date(2021,01,01,00,00,00);

    private Integer id;
    private float reading;
    private Date datetime;
    private Boolean billed;


    public Reading(){
    }

    public Reading(float reading,Date datetime)
    {
        this.reading = reading;
        this.datetime = INITIAL_DATE;
    }

    public Reading(Integer id,float reading,Date datetime)
    {
        this.id = id;
        this.reading = reading;
        this.datetime = datetime;
    }

    public Reading(Integer id,float reading,Date datetime,boolean billed)
    {
        this.id = id;
        this.reading = reading;
        this.datetime = datetime;
        this.billed = billed;
    }

    /*GETTERS AND SETTERS*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getReading() {
        return reading;
    }

    public void setReading(float reading) {
        this.reading = reading;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Boolean getBilled() {
        return billed;
    }

    public void setBilled(Boolean billed) {
        this.billed = billed;
    }

    /**/
}
