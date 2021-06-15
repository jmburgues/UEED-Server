package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.model.projections.ClientConsumption;
import edu.utn.UEEDServer.service.BillService;
import edu.utn.UEEDServer.service.ClientService;
import edu.utn.UEEDServer.service.ReadingService;
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
@RequestMapping("/clients")
public class ClientController {

    private final  ReadingService readingService;
    private final BillService billService;
    private final ClientService clientService;

    @Autowired
    public ClientController(ReadingService readingService, BillService billService, ClientService clientService) {
        this.readingService = readingService;
        this.billService = billService;
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}/bills")
    public ResponseEntity<List<Bill>> filterByClientAndDate(Authentication auth,
                                            @PathVariable Integer clientId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        verifyAuthentication(auth,clientId);
        return response(billService.filterByClientAndDate(clientId,from,to));
    }

    @GetMapping("/{clientId}/unpaid")
    public ResponseEntity<List<Bill>> getUnpaidBillClient(Authentication auth, @PathVariable Integer clientId){
        verifyAuthentication(auth,clientId);
        return response(billService.getClientUnpaid(clientId));
    }

    @GetMapping("/{clientId}/consumption")
    public ResponseEntity<ClientConsumption> getClientConsumption(Authentication auth,
                                                                  @PathVariable Integer clientId,
                                                                  @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                                  @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        verifyAuthentication(auth,clientId);
        return response(this.readingService.getClientConsumption(clientId,from,to));
    }

    @GetMapping("/{clientId}/readings")
    public ResponseEntity<List<Reading>> getClientReadingsByDate(Authentication auth,
                                                 @PathVariable Integer clientId,
                                                 @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                 @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to,
                                                 @RequestParam(value="page", defaultValue = "0") Integer page,
                                                 @RequestParam(value="size", defaultValue = "10") Integer size){
        verifyAuthentication(auth,clientId);
        return response(readingService.getClientReadingsByDate(clientId,from,to,page,size));
    }

    private void verifyAuthentication(Authentication auth, Integer clientId){
        Client loggedClient = this.clientService.getByUsername( ((UserDTO) auth.getPrincipal()).getUsername() );
        if(!loggedClient.getId().equals(clientId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden. Verify clientId.");
    }
}
