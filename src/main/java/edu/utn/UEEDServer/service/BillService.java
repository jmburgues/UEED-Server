package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    public List<Bill> getClientUnpaid(Integer clientId) {

        List<Bill> list = billRepository.getUnpaidByClient(clientId);

        if(list.isEmpty()) {
            String message = "No unpaid bills found under client id: " + clientId;
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,message);
        }

        return list;
    }

    public List<Bill> getAddressUnpaid(Integer addressId) {

        List<Bill> list = billRepository.getUnpaidByAddress(addressId);

        if(list.isEmpty()) {
            String message = "No unpaid bills found under address id: " + addressId;
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,message);
        }

        return list;
    }

    public List<Bill> filterByClientAndDate(Integer clientId, Date from, Date to) {

        if(from.after(to))
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
}
