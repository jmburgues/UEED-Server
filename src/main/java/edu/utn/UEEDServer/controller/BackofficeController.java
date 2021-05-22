package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.service.*;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/backoffice")
public class BackofficeController {

    private static final String RATE_PATH = "rate";
    private static final String ADDRESS_PATH = "address";
    private static final String METER_PATH = "meter";

    private RateService rateService;
    private AddressService addressService;
    private MeterService meterService;
    private BillService billService;
    private ReadingService readingService;

    @Autowired
    public BackofficeController(RateService rateService, AddressService addressService, MeterService meterService, BillService billService, ReadingService readingService) {
        this.rateService = rateService;
        this.addressService = addressService;
        this.meterService = meterService;
        this.billService = billService;
        this.readingService = readingService;
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

    @PostMapping("/address")
    public PostResponse addAddress(@RequestBody Address address) {
        Address added = addressService.add(address);

        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(ADDRESS_PATH, added.getAddressId())).
                build();
    }

    @PutMapping("/address")
    public PostResponse updateAddress(@RequestBody Address address) {
        Address updated = addressService.update(address);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(ADDRESS_PATH, updated.getAddressId().toString()))
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
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") LocalDateTime from,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") LocalDateTime to){
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
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime to){
        return this.readingService.getAddressReadingsByDate(addressId,from,to);
    }


/* CLIENT ENDPOINTS */

    @GetMapping("/client/topconsumers")
    public List<Client> getTopConsumers( // Hacer un proyection
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime from,
                                               @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime to){
        return this.readingService.getTopConsumers(from,to);
    }
}

