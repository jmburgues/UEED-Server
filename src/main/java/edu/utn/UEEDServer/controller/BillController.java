package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")
public class BillController {

    BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }
}
