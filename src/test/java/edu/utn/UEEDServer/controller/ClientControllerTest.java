package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.BillService;
import edu.utn.UEEDServer.service.ClientService;
import edu.utn.UEEDServer.service.ReadingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.aClient;
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

    @Mock
    private Authentication auth;

    private Client loggedClient;

    private ResponseStatusException expectedException;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        clientController = new ClientController(readingService, billService, clientService);
        this.expectedException = null;
        this.loggedClient=aClient();

    }

    @Test
    public void testFilterByClientAndDateHappyPath() {

        List<Bill>listOfBills = new ArrayList<>();
        listOfBills.add(Bill.builder().billId(1).build());

        try {
            Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
            Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

            when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("User1").build());
            when(clientService.getByUsername("User1")).thenReturn(Client.builder().id(1).build());
            when(billService.filterByClientAndDate(1, from,to)).thenReturn(listOfBills);

          ResponseEntity<List<Bill>> response = clientController.filterByClientAndDate(auth, 1, from, to);

            Assert.assertEquals(listOfBills.get(0).getBillId(), response.getBody().get(0).getBillId());
            Assert.assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

        } catch (ParseException e) {
            Assert.fail("parsing dates failed.");
        }
    }

    @Test
    public void testFilterByClientAndDate403Forbidden() {



        try {
            Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
            Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
           loggedClient.setId(2);
            when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("User1").build());
            when(clientService.getByUsername("User1")).thenReturn(Client.builder().id(2).build());

            try {
                clientController.filterByClientAndDate(auth, 1, from, to);
            }catch(ResponseStatusException e){
                expectedException = e;
            }

            Assert.assertEquals(HttpStatus.FORBIDDEN, expectedException.getStatus());
            Assert.assertEquals(ResponseStatusException.class,expectedException.getClass());

        } catch (ParseException e) {
            Assert.fail("parsing dates failed.");
        }
    }

    @Test
    public void testFilterByClientAndDate204() {

        List<Bill> listOfBills = new ArrayList<>();

        try {
            Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
            Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

            when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("User1").build());
            when(clientService.getByUsername("User1")).thenReturn(Client.builder().id(1).build());
            when(billService.filterByClientAndDate(1, from,to)).thenReturn(listOfBills);

            ResponseEntity<List<Bill>> newList = clientController.filterByClientAndDate(auth, 1, from, to);

            Assert.assertEquals(listOfBills.isEmpty(), newList.getBody().isEmpty());

        } catch (ParseException e) {
            Assert.fail("parsing dates failed.");
        }
    }

    @Test
    public void testGetUnpaidBillClientHappyPath(){

        List<Bill> listOfBills = new ArrayList<>();

        when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("user").build());
        when(clientService.getByUsername("user")).thenReturn(Client.builder().id(1).build());
        when(billService.getClientUnpaid(1)).thenReturn(listOfBills);

        ResponseEntity<List<Bill>> response = clientController.getUnpaidBillClient(auth,1);

        Assert.assertEquals(!listOfBills.isEmpty(), !response.getBody().isEmpty());
    }

    @Test
    public void testGetUnpaidBillClient403Forbidden(){


        when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("user").build());
        when(clientService.getByUsername("user")).thenReturn(Client.builder().id(2).build());

        try {
            clientController.getUnpaidBillClient(auth,1);
        }catch(ResponseStatusException e){
            expectedException = e;
        }
        Assert.assertNotNull(expectedException);
        Assert.assertEquals(HttpStatus.FORBIDDEN, expectedException.getStatus());


    }

    @Test
    public void testGetUnpaidBillClient204(){

        List<Bill> listOfBills = new ArrayList<>();

        when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("user").build());
        when(clientService.getByUsername("user")).thenReturn(Client.builder().id(1).build());
        when(billService.getClientUnpaid(1)).thenReturn(listOfBills);

        ResponseEntity<List<Bill>> response = clientController.getUnpaidBillClient(auth,1);

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assert.assertEquals(listOfBills.isEmpty(), response.getBody().isEmpty());
    }
}

