package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.ConsumersDTO;
import edu.utn.UEEDServer.service.*;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/backoffice")
public class BackofficeController {

    private static final String RATE_PATH = "backoffice/rate";
    private static final String ADDRESS_PATH = "backoffice/address";
    private static final String METER_PATH = "backoffice/meter";
    private static final String CLIENT_PATH = "backoffice/client";

    private RateService rateService;
    private AddressService addressService;
    private MeterService meterService;
    private BillService billService;
    private ReadingService readingService;
    private ClientService clientService;

    @Autowired
    public BackofficeController(RateService rateService, AddressService addressService, MeterService meterService, BillService billService, ReadingService readingService, ClientService clientService) {
        this.rateService = rateService;
        this.addressService = addressService;
        this.meterService = meterService;
        this.billService = billService;
        this.readingService = readingService;
        this.clientService = clientService;
    }

/* RATES ENDPOINTS */

    @GetMapping("/rate")
    public List<Rate> getAllRate() {
        return rateService.getAll();
    }

    @GetMapping("/rate/{id}")
    public Rate getByIdRate(@PathVariable Integer id) {
        return rateService.getById(id);
    }

    @PostMapping("/rate")
    public PostResponse addRate(@RequestBody Rate rate) {
        Rate added = rateService.add(rate);
        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(RATE_PATH, added.getId())).
                build();
    }

    @PutMapping("/rate")
    public PostResponse updateRate(@RequestBody Rate rate) {

        Rate saved = rateService.update(rate);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(RATE_PATH, saved.getId().toString()))
                .build();
    }

    // Shall we implement DELETE method??
    @DeleteMapping("/rate/{id}")
    public void deleteRate(@PathVariable Integer id) {
        rateService.delete(id);
    }


/* ADDRESSES ENDPOINTS */

    @GetMapping("/address")
    public List<Address> getAllAddress() {
        return addressService.getAll();
    }

    @GetMapping("/address/{id}")
    public Address getByIdAddress(@PathVariable Integer id) {
        return addressService.getById(id);
    }

    @PostMapping("/client/{clientId}/address")
    public PostResponse addAddress(@PathVariable Integer clientId, @RequestBody Address address) {
        Address newAddress = addressService.add(clientId, address);

        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(CLIENT_PATH+"/"+clientId, ADDRESS_PATH+ "/"+ newAddress.getAddressId())).
                build();
    }

    @PutMapping("/client/{clientId}/address")
    public PostResponse updateAddress(@PathVariable Integer clientId, @RequestBody Address address) {
        Client updated = addressService.update(clientId, address);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(CLIENT_PATH, clientId))
                .build();
    }

    @DeleteMapping("/address/{addressId}")
    public void deleteAddress(@PathVariable Integer addressId) {
        addressService.delete(addressId);
    }


/* METER ENDPOINTS */

    @GetMapping
    public List<Meter> getAllMeter() {
        return this.meterService.getAll();
    }

    @GetMapping("/meter/{serialNumber}")
    public Meter getByIdMeter(@PathVariable String serialNumber) {
        return this.meterService.getById(serialNumber);
    }

    @PostMapping("/meter")
    public PostResponse addMeter(@RequestBody Meter newMeter) {
        Meter saved = meterService.add(newMeter);

        return PostResponse.builder().
                status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(METER_PATH, saved.getSerialNumber().toString()))
                .build();
    }

    @PutMapping("/meter")
    public PostResponse updateMeter(@RequestBody Meter meter) {
        Meter m = meterService.update(meter);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(METER_PATH, m.getSerialNumber().toString()))
                .build();
    }

    @DeleteMapping("/meter/{serialNumber}")
    public void deleteMeter(@PathVariable String serialNumber) {
        this.meterService.delete(serialNumber);
    }


/* BILL ENDPOINTS */

    @GetMapping("/client/{clientId}/bills") // VER QUERY DSL PARA LOS DIFERENTES FILTERS
    public List<Bill> filterByDate(@PathVariable Integer clientId,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        return this.billService.filterByClientAndDate(clientId,from,to);
    }

    @GetMapping("/client/{clientId}/bills/unpaid")
    public List<Bill>getUnpaidBillClient(@PathVariable Integer clientId){
        return billService.getClientUnpaid(clientId);
    }

    @GetMapping("/address/{addressId}/bills/unpaid")
    public List<Bill>getUnpaidBillAddress(@PathVariable Integer addressId){
            return billService.getAddressUnpaid(addressId);
    }

/* READING ENDPOINTS */

    @GetMapping("/address/{addressId}/readings")
    public List<Reading> getAddressReadings(@PathVariable Integer addressId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        return this.readingService.getAddressReadingsByDate(addressId,from,to);
    }


/* CLIENT ENDPOINTS */

    @GetMapping("/client/topconsumers")
    public List<ConsumersDTO> getTopConsumers( // Hacer un proyection
                                               @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                               @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
        return this.readingService.getTopConsumers(from,to);
    }
}

