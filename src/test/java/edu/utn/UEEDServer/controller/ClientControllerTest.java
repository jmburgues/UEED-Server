package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.BillService;
import edu.utn.UEEDServer.service.ClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import edu.utn.UEEDServer.service.ReadingService;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class ClientControllerTest {
    @Mock
    private ReadingService readingService;
    @Mock
    private BillService billService;
    @Mock
    private ClientService clientService;

    private ClientController clientController;

    @Before
    public void setUp() {
        initMocks(this);
        clientController = new ClientController(readingService, billService, clientService);
    }

    @Test
    public void testFilterByClientAndDateHappyPath() {
        Authentication auth = mock(Authentication.class);
        List<Bill> listOfBills = new ArrayList<>();
        listOfBills.add(Bill.builder().billId(1).build());

        try {
            Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
            Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

            when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("User1").build());
            when(clientService.getByUsername("User1")).thenReturn(Client.builder().id(1).build());
            when(billService.filterByClientAndDate(1, from,to)).thenReturn(listOfBills);

            List<Bill> newList = clientController.filterByClientAndDate(auth, 1, from, to);

            Assert.assertEquals(listOfBills.get(0).getBillId(), newList.get(0).getBillId());

        } catch (ParseException e) {
            Assert.fail("parsing dates failed.");
        }
    }

    /*@Test
    public void testFilterByClientAndDateNoAuth() {*/
}

