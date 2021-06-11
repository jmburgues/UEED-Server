package edu.utn.UEEDServer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.ReadingDTO;
import edu.utn.UEEDServer.model.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {



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

    public static Bill aBill() {

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

    public static Brand aBrand() {

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

    public static Client aClient() {

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

    public static Meter aMeter(){

        return Meter.builder()
                .model(aModel())
                .lastReading(LocalDateTime.now())
                .accumulatedConsumption(10)
                .address(aAddress())
                .serialNumber("aaa111")
                .password("1234")
                .readings(List.of())
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

    public static Rate aRate() {

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

    public static User aUser() {

        return User.builder()
                .name("name")
                .surname("surname")
                .username("username")
                .password("pass")
                .employee(false)
                .build();
    }

    public static UserDTO anEmployee(){

        return  UserDTO
                .builder()
                .username("user")
                .name("name")
                .surname("surname")
                .employee(true)
                .build();

    }

    public static UserDTO aUserDTO(){
        return  UserDTO
                .builder()
                .username("user")
                .name("name")
                .surname("surname")
                .employee(false)
                .build();
    }

    public static ReadingDTO aReadingDTO() {

        return ReadingDTO.builder()
                .meterSerialNumber("aaa111")
                .readDate(LocalDateTime.now())
                .password("1234")
                .totalKw(15F)
                .build();
    }








}
