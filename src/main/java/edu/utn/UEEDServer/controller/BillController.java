package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
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

}
