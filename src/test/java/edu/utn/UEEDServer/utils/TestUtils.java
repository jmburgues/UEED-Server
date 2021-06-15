package edu.utn.UEEDServer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.AddressDTO;

import edu.utn.UEEDServer.model.dto.ReadingDTO;
import edu.utn.UEEDServer.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {



    // Address
    public static String aAddressJSON(){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(anAddress());
    }

    public static Address anAddress(){

        return Address.builder()
                .addressId(1)
                .street("calle falsa")
                .number(1234)
                .rate(aRate())
                .client(aClient())
                .build();
    }

    public static AddressDTO anAddressDTO(){

        return AddressDTO.builder()
                .street("calle falsa")
                .number(1234)
                .clientId(1)
                .rateId(1)
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
                .billedDate(null)
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
                .lastReading(null)
                .accumulatedConsumption(10)
                .address(anAddress())
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
                .kwPrice(10F)
                .build();
    }

    //Readings

    public static String aReadingJSON()
    {
        final Gson gson= new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(aReading());
    }

    public static Reading aReading() {

        return Reading.builder()
                .readingId(1)
                .bill(new Bill())
                .readDate(null)
                .readingPrice(2F)
                .totalKw(3F)
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
                .username("username")
                .password("pass")
                .employee(false)
                .build();
    }

    public static UserDTO anEmployee(){

        return  UserDTO
                .builder()
                .username("user")
                .employee(true)
                .build();

    }

    public static UserDTO aUserDTO(){
        return  UserDTO
                .builder()
                .username("user")
                .employee(false)
                .build();
    }

    public static ReadingDTO aReadingDTO() {

        return ReadingDTO.builder()
                .meterSerialNumber("aaa111")
                .readDate(null)
                .password("1234")
                .totalKw(15F)
                .build();
    }




    public static List anEmptyList(){

        return new ArrayList<>();
    }






}
