package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ClientService clientService;
    private final AddressService addressService;

    @Autowired
    public BillService(BillRepository billRepository, ClientService clientService, AddressService addressService) {
        this.billRepository = billRepository;
        this.clientService = clientService;
        this.addressService = addressService;
    }

    public List<Bill> getClientUnpaid(Integer clientId) {
        clientService.getById(clientId);
        return billRepository.getUnpaidByClient(clientId);
    }

    public List<Bill> getAddressUnpaid(Integer addressId) {
        addressService.getById(addressId);
        return billRepository.getUnpaidByAddress(addressId);
    }

    public List<Bill> filterByClientAndDate(Integer clientId, Date from, Date to) {

        if(from.after(to))
            throw new IllegalArgumentException("Date From (" + from + ") can not be after date To (" + to + ")");

         return billRepository.dateAndClientFilter(clientId,from,to);
    }
}
