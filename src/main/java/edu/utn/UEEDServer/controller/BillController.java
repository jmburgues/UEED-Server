package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public PostResponse add(@RequestBody Bill bill)
    {
        return billService.add(bill);
    }

    @GetMapping
    public List<Bill>getAll()
    {
        return billService.getAll();
    }

    @GetMapping("/{id}")
    public Bill getById(@PathVariable Integer id)
    {
        return billService.getById(id);
    }

    // [PROG - ITEM 2] As an employee I want to query client's bills filtered by date ranges.
    @GetMapping("/filter")
    public List<Bill> filter(@RequestParam(required = false) Integer clientId,
                             @RequestParam @DateTimeFormat(pattern="yyyy-MM") LocalDateTime from,
                             @RequestParam @DateTimeFormat(pattern="yyyy-MM") LocalDateTime to){
        return this.billService.filter(clientId,from,to);
    }
}
