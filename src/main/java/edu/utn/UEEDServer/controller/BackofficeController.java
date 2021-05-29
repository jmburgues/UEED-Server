package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.ConsumersDTO;
import static edu.utn.UEEDServer.utils.Constants.*;

import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.*;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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
    public List<Rate> getAllRate(Authentication auth) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return rateService.getAll();
    }

    @GetMapping("/rate/{id}")
    public Rate getByIdRate(Authentication auth, @PathVariable Integer id) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return rateService.getById(id);
    }

    @PostMapping("/rate")
    public PostResponse addRate(Authentication auth, @RequestBody Rate rate) {
        Rate added = rateService.add(rate);
        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(RATE_PATH, added.getId())).
                build();
    }

    @PutMapping("/rate")
    public PostResponse updateRate(Authentication auth, @RequestBody Rate rate) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");

        Rate saved = rateService.update(rate);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(RATE_PATH, saved.getId().toString()))
                .build();
    }

    // Shall we implement DELETE method??
    @DeleteMapping("/rate/{id}")
    public void deleteRate(Authentication auth, @PathVariable Integer id) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        rateService.delete(id);
    }


/* ADDRESSES ENDPOINTS */

    @GetMapping("/address")
    public List<Address> getAllAddress(Authentication auth) {
        return addressService.getAll();
    }

    @GetMapping("/address/{id}")
    public Address getByIdAddress(Authentication auth, @PathVariable Integer id) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return addressService.getById(id);
    }

    @PostMapping("/client/{clientId}/address")
    public PostResponse addAddress(Authentication auth, @PathVariable Integer clientId, @RequestBody Address address) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Address newAddress = addressService.add(clientId, address);

        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(CLIENT_PATH+"/"+clientId, ADDRESS_PATH+ "/"+ newAddress.getAddressId())).
                build();
    }

    @PutMapping("/client/{clientId}/address")
    public PostResponse updateAddress(Authentication auth, @PathVariable Integer clientId, @RequestBody Address address) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Client updated = addressService.update(clientId, address);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(CLIENT_PATH, clientId))
                .build();
    }

    @DeleteMapping("/address/{addressId}")
    public void deleteAddress(Authentication auth, @PathVariable Integer addressId) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        addressService.delete(addressId);
    }


/* METER ENDPOINTS */

    @GetMapping
    public List<Meter> getAllMeter(Authentication auth) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.meterService.getAll();
    }

    @GetMapping("/meter/{serialNumber}")
    public Meter getByIdMeter(Authentication auth, @PathVariable String serialNumber) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.meterService.getById(serialNumber);
    }

    @PostMapping("/meter")
    public PostResponse addMeter(Authentication auth, @RequestBody Meter newMeter) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Meter saved = meterService.add(newMeter);

        return PostResponse.builder().
                status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(METER_PATH, saved.getSerialNumber().toString()))
                .build();
    }

    @PutMapping("/meter")
    public PostResponse updateMeter(Authentication auth, @RequestBody Meter meter) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Meter m = meterService.update(meter);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(METER_PATH, m.getSerialNumber().toString()))
                .build();
    }

    @DeleteMapping("/meter/{serialNumber}")
    public void deleteMeter(Authentication auth, @PathVariable String serialNumber) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        this.meterService.delete(serialNumber);
    }


/* BILL ENDPOINTS */

    @GetMapping("/client/{clientId}/bills") // VER QUERY DSL PARA LOS DIFERENTES FILTERS
    public List<Bill> filterByDate(Authentication auth,
                                   @PathVariable Integer clientId,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.billService.filterByClientAndDate(clientId,from,to);
    }

    @GetMapping("/client/{clientId}/bills/unpaid")
    public List<Bill>getUnpaidBillClient(Authentication auth, @PathVariable Integer clientId){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return billService.getClientUnpaid(clientId);
    }

    @GetMapping("/address/{addressId}/bills/unpaid")
    public List<Bill>getUnpaidBillAddress(Authentication auth, @PathVariable Integer addressId){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
            return billService.getAddressUnpaid(addressId);
    }

/* READING ENDPOINTS */

    @GetMapping("/address/{addressId}/readings")
    public List<Reading> getAddressReadings(Authentication auth,
                                            @PathVariable Integer addressId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.readingService.getAddressReadingsByDate(addressId,from,to);
    }


/* CLIENT ENDPOINTS */

    @GetMapping("/client/topconsumers")
    public List<ConsumersDTO> getTopConsumers( Authentication auth,
                                               @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                               @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.readingService.getTopConsumers(from,to);
    }
}

