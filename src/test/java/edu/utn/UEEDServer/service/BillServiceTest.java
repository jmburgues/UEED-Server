package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.repository.BillRepository;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.aBill;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BillServiceTest {

    @Mock
    BillRepository billRepository;
    @Mock
    ClientService clientService;
    @Mock
    AddressService addressService;

    private BillService billService;

    @BeforeEach
    public void setUp(){
        initMocks(this);
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
