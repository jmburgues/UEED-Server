package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.service.BillService;
import edu.utn.UEEDServer.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    ReadingService readingService;
    BillService billService;

    @Autowired
    public ClientController(ReadingService readingService, BillService billService) {
        this.readingService = readingService;
        this.billService = billService;
    }

    @GetMapping("/{clientId}/bill") // VER QUERY DSL PARA LOS DIFERENTES FILTERS
    public List<Bill> filterByClientAndDate(@PathVariable Integer clientId,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        return this.billService.filterByClientAndDate(clientId,from,to);
    }

    @GetMapping("/{clientId}/unpaid")
    public List<Bill>getUnpaidBillClient(@PathVariable Integer clientId){
        return billService.getClientUnpaid(clientId);
    }

    @GetMapping("/{clientId}/consumption")
    public Map<String, Float> getClientConsumption(@PathVariable Integer clientId,
                                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        return this.readingService.getClientConsumption(clientId,from,to);
    }

    @GetMapping("/{clientId}/readings")
    public List<Reading> getClientReadingsByDate(@PathVariable Integer clientId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        return this.readingService.getClientReadingsByDate(clientId,from,to);
    }
}
