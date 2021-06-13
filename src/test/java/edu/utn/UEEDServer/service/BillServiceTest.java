package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.repository.BillRepository;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.aBill;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BillServiceTest {


    BillRepository billRepository;

    ClientService clientService;

    AddressService addressService;

    BillService billService;

    @Before
    public void setUp(){
        billRepository=mock(BillRepository.class);
        clientService=mock(ClientService.class);
        addressService=mock(AddressService.class);
        billService = new BillService(billRepository,clientService,addressService);
    }

    @Test
    public void getClientUnpaidTest(){
        when(billRepository.getUnpaidByClient(anyInt())).thenReturn(List.of(aBill()));

        List<Bill>actualBillList=billService.getClientUnpaid(anyInt());

        Assert.assertEquals(List.of(aBill()),actualBillList);
    }
    @Test
    public void getAddressUnpaid(){
        when(billRepository.getUnpaidByAddress(anyInt())).thenReturn(List.of(aBill()));
        List<Bill>actualList = billService.getAddressUnpaid(anyInt());

        Assert.assertEquals(List.of(aBill()),actualList);
    }
    @SneakyThrows
    @Test
    public void filterByClientAndDate(){
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(billRepository.dateAndClientFilter(clientId,from,to)).thenReturn(List.of(aBill()));
       List<Bill>actualList = billService.filterByClientAndDate(clientId,from,to);

       Assert.assertEquals(List.of(aBill()),actualList);
    }
    @SneakyThrows
    @Test
    public void filterByClientAndDate_IllegalArgumentException(){
        Integer clientId = 1;
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        Assert.assertThrows(IllegalArgumentException.class,()->billService.filterByClientAndDate(clientId,from,to));
    }


}
