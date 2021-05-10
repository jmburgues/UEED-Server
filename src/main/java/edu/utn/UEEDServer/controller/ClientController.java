package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public PostResponse add(@RequestBody Client client)
    {
        return clientService.add(client);
    }

    @GetMapping
    public List<Client> getAll()
    {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public Client getById(@PathVariable Integer id)
    {
        return clientService.getById(id);
    }
}
