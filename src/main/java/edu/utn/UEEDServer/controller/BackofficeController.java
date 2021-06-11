package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.AddressDTO;
import edu.utn.UEEDServer.model.dto.ConsumersDTO;

import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.*;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import edu.utn.UEEDServer.utils.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.Response.response;

@RestController
@RequestMapping("/backoffice")
public class BackofficeController {

    private static final String RATE_PATH = "/rate";
    private static final String ADDRESS_PATH = "/address";
    private static final String METER_PATH = "/meter";
    private static final String CLIENT_PATH = "/client";

    private final RateService rateService;
    private final AddressService addressService;
    private final MeterService meterService;
    private final BillService billService;
    private final ReadingService readingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BackofficeController(RateService rateService, AddressService addressService,
                                MeterService meterService, BillService billService,
                                ReadingService readingService, ModelMapper modelMapper) {
        this.rateService = rateService;
        this.addressService = addressService;
        this.meterService = meterService;
        this.billService = billService;
        this.readingService = readingService;
        this.modelMapper = modelMapper;
    }

/* RATES ENDPOINTS */

    @GetMapping(RATE_PATH)
    public ResponseEntity<List<Rate>> getAllRate(Authentication auth) {
        verifyAuthentication(auth);
        return response(rateService.getAll());
    }

    @GetMapping(RATE_PATH + "/{id}")
    public ResponseEntity<Rate> getByIdRate(Authentication auth, @PathVariable Integer id) {
        verifyAuthentication(auth);
        return response(this.rateService.getById(id));
    }

    @PostMapping(RATE_PATH)
    public ResponseEntity addRate(Authentication auth, @RequestBody Rate rate) {
        verifyAuthentication(auth);
        Rate added = rateService.add(rate);
       return ResponseEntity.created(EntityURLBuilder.buildURL(added.getId())).build();
    }

    @PutMapping(RATE_PATH)
    public ResponseEntity updateRate(Authentication auth, @RequestBody Rate rate) {
        verifyAuthentication(auth);
        if(rateService.getById(rate.getId())!=null)
        return ResponseEntity.ok(rate); // si ya existe actualizo y devuelvo ok

        else   //sino lo creo y devuelvo el nuevo recurso
            return ResponseEntity.created(EntityURLBuilder.buildURL(rate.getId())).build();

    }

    @DeleteMapping(RATE_PATH + "/{id}")
    public void deleteRate(Authentication auth, @PathVariable Integer id) {
        verifyAuthentication(auth);
        rateService.delete(id);
    }


/* ADDRESSES ENDPOINTS */

    @GetMapping(ADDRESS_PATH)
    public  ResponseEntity<List<Address>> getAllAddress(Authentication auth) {
        return response(addressService.getAll());
    }

    @GetMapping(ADDRESS_PATH + "/{id}")
    public ResponseEntity<Address> getByIdAddress(Authentication auth, @PathVariable Integer id) {
        verifyAuthentication(auth);
        return response(addressService.getById(id));
    }

    @PostMapping(ADDRESS_PATH)
    public ResponseEntity addAddress(Authentication auth, @RequestBody AddressDTO addressDTO) {
        verifyAuthentication(auth);
        Address newAddress = modelMapper.map(addressDTO,Address.class);
        Address savedAddress = addressService.add(addressDTO.getClientId(), addressDTO.getRateId(), newAddress);

        return ResponseEntity.created(EntityURLBuilder.buildURL(savedAddress.getAddressId())).build();
    }

    @PutMapping(ADDRESS_PATH)
    public ResponseEntity updateAddress(Authentication auth, @RequestBody AddressDTO addressDTO) {
        verifyAuthentication(auth);
        Address newAddress = modelMapper.map(addressDTO,Address.class);
        addressService.update(addressDTO.getClientId(), addressDTO.getRateId(), newAddress);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(ADDRESS_PATH + "/{addressId}")
    public void deleteAddress(Authentication auth, @PathVariable Integer addressId) {
        verifyAuthentication(auth);
        addressService.delete(addressId);
    }


/* METER ENDPOINTS */

    @GetMapping(METER_PATH)
    public ResponseEntity<List<Meter>> getAllMeter(Authentication auth) {
        verifyAuthentication(auth);
        return response(this.meterService.getAll());
    }

    @GetMapping(METER_PATH + "/{serialNumber}")
    public ResponseEntity<Meter> getByIdMeter(Authentication auth, @PathVariable String serialNumber) {
        verifyAuthentication(auth);
        return response(this.meterService.getById(serialNumber));
    }

    @PostMapping(METER_PATH)
    public ResponseEntity addMeter(Authentication auth, @RequestBody Meter newMeter) {
        verifyAuthentication(auth);
        Meter saved = meterService.add(newMeter);
        return ResponseEntity
                .created(EntityURLBuilder.buildURL(saved.getSerialNumber())).build();
    }

    @PutMapping(METER_PATH)
    public ResponseEntity updateMeter(Authentication auth, @RequestBody Meter meter) {
        verifyAuthentication(auth);
        meterService.update(meter);

        if(meterService.getById(meter.getSerialNumber())!=null)
        return ResponseEntity.ok().build(); //si esta actualizo

        else return ResponseEntity //sino lo creo y devuelvo el recurso
                .created(EntityURLBuilder.buildURL(meter.getSerialNumber())).build();
    }

    @DeleteMapping(METER_PATH + "/{serialNumber}")
    public void deleteMeter(Authentication auth, @PathVariable String serialNumber) {
        verifyAuthentication(auth);
        this.meterService.delete(serialNumber);
    }


/* BILL ENDPOINTS */

    @GetMapping(CLIENT_PATH + "/{clientId}/bills") // VER QUERY DSL PARA LOS DIFERENTES FILTERS
    public ResponseEntity<List<Bill>> filterByDate(Authentication auth,
                                   @PathVariable Integer clientId,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        verifyAuthentication(auth);
        return response(this.billService.filterByClientAndDate(clientId,from,to));
    }

    @GetMapping(CLIENT_PATH + "/{clientId}/bills/unpaid")
    public ResponseEntity<List<Bill>> getUnpaidBillClient(Authentication auth, @PathVariable Integer clientId){
        verifyAuthentication(auth);
        return response(billService.getClientUnpaid(clientId));
    }

    @GetMapping(ADDRESS_PATH + "/{addressId}/bills/unpaid")
    public ResponseEntity<List<Bill>> getUnpaidBillAddress(Authentication auth, @PathVariable Integer addressId){
        verifyAuthentication(auth);
        return response(billService.getAddressUnpaid(addressId));
    }

/* READING ENDPOINTS */

    @GetMapping(ADDRESS_PATH + "/{addressId}/readings")
    public ResponseEntity<List<Reading>> getAddressReadings(Authentication auth,
                                            @PathVariable Integer addressId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        verifyAuthentication(auth);
        return response(this.readingService.getAddressReadingsByDate(addressId,from,to));
    }


/* CLIENT ENDPOINTS */

    @GetMapping(CLIENT_PATH + "/topconsumers")
    public ResponseEntity<List<ConsumersDTO>> getTopConsumers( Authentication auth,
                                               @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                               @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
        verifyAuthentication(auth);
        return response(this.readingService.getTopConsumers(from,to));
    }


/* MISC */

    private void verifyAuthentication(Authentication auth){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
    }
}

