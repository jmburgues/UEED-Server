package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.repository.BillRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class BillService {

    private static final String BILL_PATH="bill";
    BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
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
}
