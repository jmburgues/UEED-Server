package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.BillRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

        return billRepository.findAll();
    }

    public Bill getById(Integer id) {

        return billRepository.findById(id).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Bill> getByClientId(Integer clientId){
        return this.billRepository.getByClientId(clientId);
    }

    public List<Bill> getUnpaid(Integer clientId,Boolean paid) {
        if (clientId == null) {
            return billRepository.getUnpaid();
        } else {
            return billRepository.getUnpaidByClient(clientId);
        }
    }

    public List<Bill> filter(Integer clientId, LocalDateTime from, LocalDateTime to) {
        if(clientId == null)
            return this.billRepository.dateFilter(from,to);
        else
            return this.billRepository.dateAndClientFilter(clientId,from,to);
    }
}
