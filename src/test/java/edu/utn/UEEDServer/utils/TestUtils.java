package edu.utn.UEEDServer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.UEEDServer.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class TestUtils {

    //TODO ver si hay alguna manera de no repetir tanto codigo acá

    // Address
    public static String aAddressJSON(){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aAddress());
    }

    public static Address aAddress(){

        return Address.builder()
                .addressId(1)
                .street("calle1")
                .number(2)
                .build();
    }

    //Bills
    public static String aBillJSON(){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aBill());
    }

    private static Bill aBill() {

        return Bill.builder()
                .billId(1)
                .billedDate(LocalDateTime.now())
                .build();

    }

    //Brands

    public static String aBrandJSON(){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aBrand());
    }

    private static Brand aBrand() {

        return Brand.builder()
                .id(1)
                .name("brand")
                .build();

    }

    //Clients

    public static String aClientJSON(){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aClient());
    }

    private static Client aClient() {

        return Client.builder()
                .id(1)
                .bills(new ArrayList<>())
                .user(new User())
                .build();

    }

    //Meters

    public static String aMeterJSON(){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aMeter());

    }

    private static Meter aMeter() {

        return Meter.builder()
                .serialNumber("1")
                .lastReading(LocalDateTime.now())
                .model(new Model())
                .readings(new ArrayList<>())
                .accumulatedConsumption(0)
                .build();
    }

    //Models

    public static String aModelJSON()
    {
        final Gson gson= new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aModel());
    }

    private static Model aModel() {

        return Model.builder()
                .id(1)
                .name("model")
                .brand(new Brand())
                .build();
    }

    //Rates

    public static String aRateJSON()
    {
        final Gson gson= new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aRate());
    }

    private static Rate aRate() {

        return Rate.builder()
                .id(1)
                .category("category")
                .kwPrice(new Random().nextFloat())
                .build();
    }

    //Readings

    public static String aReadingJSON()
    {
        final Gson gson= new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aReading());
    }

    private static Reading aReading() {

        return Reading.builder()
                .readingId(1)
                .bill(new Bill())
                .readDate(LocalDateTime.now())
                .readingPrice(new Random().nextFloat())
                .totalKw(new Random().nextFloat())
                .build();
    }

    //Users

    public static String aUserJSON()
    {
        final Gson gson= new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aUser());
    }

    private static User aUser() {

        return User.builder()
                .name("name")
                .surname("surname")
                .username("username")
                .password("pass")
                .employee(false)
                .build();
    }






}
