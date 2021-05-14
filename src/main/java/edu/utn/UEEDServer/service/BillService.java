package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Brand;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.BillRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillService {

    private static final String BILL_PATH="bill";
    BillRepository billRepository;
    ClientService clientService;

    @Autowired
    public BillService(BillRepository billRepository,ClientService clientService) {
        this.billRepository = billRepository;
        this.clientService=clientService;
    }

    public PostResponse add(Bill bill) {

        Bill b = billRepository.save(bill);
        return PostResponse.builder()
                .status(HttpStatus.CREATED).url(EntityURLBuilder.buildURL(BILL_PATH,b.getBillId()))
                .build();
    }

    public List<Bill> getAll() {

        List<Bill> list = billRepository.findAll();
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Bill list is empty");
        return list;
    }

    public Bill getById(Integer billId) {

        return billRepository.findById(billId).
                orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "No bills found under id: " + billId));
    }

    public List<Bill> getByClientId(Integer clientId){
        Client client = this.clientService.getById(clientId);

        List<Bill> list = this.billRepository.getByClientId(client.getId());
        if(list.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"No bills found under client id: " + clientId);
        return list;
    }

    public List<Bill> getUnpaid(Integer clientId,Boolean paid) {

        List<Bill> list;

        if (clientId == null)
            list = billRepository.getUnpaid();
        else
            list = billRepository.getUnpaidByClient(clientId);

        if(list.isEmpty()) {
            String message = (clientId == null) ? "No unpaid bills found" : "No unpaid bills found under client id: " + clientId;
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,message);
        }

        return list;
    }

    public List<Bill> filter(Integer clientId, LocalDateTime from, LocalDateTime to) {

        if(from.isAfter(to))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Date From (" + from + ") can not be after date To (" + to + ")");

        List<Bill> list;

        if(clientId == null)
            list = this.billRepository.dateFilter(from,to);
        else
            list = this.billRepository.dateAndClientFilter(clientId,from,to);

        if(list.isEmpty()) {
            String message = (clientId == null) ? "No bills found for selected dates. From: " + from + ", To:" + to
                                                : "No bills found under client id: " + clientId + ". From: " + from + ", To:" + to;
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,message);
        }

        return list;
    }

    public void delete(Integer billId) {
        if(!billRepository.existsById(billId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No bill found under id: " + billId);
        billRepository.deleteById(billId);
    }

    public PostResponse update(Bill bill) {

        this.getById(bill.getBillId());

        Bill saved = billRepository.save(bill);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(BILL_PATH,saved.getBillId().toString()))
                .build();
    }
}
