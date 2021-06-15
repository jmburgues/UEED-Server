package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.model.projections.ClientConsumption;
import edu.utn.UEEDServer.service.BillService;
import edu.utn.UEEDServer.service.ClientService;
import edu.utn.UEEDServer.service.ReadingService;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ClientControllerTest {

    private ReadingService readingService;

    private BillService billService;

    private ClientService clientService;

    private ClientController clientController;


    private Authentication auth;

    private Client loggedClient;

    private ResponseStatusException expectedException;

    @Before
    public void setUp() {
        readingService=mock(ReadingService.class);
        billService=mock(BillService.class);
        clientService=mock(ClientService.class);
        auth=mock(Authentication.class);
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

            assertEquals(listOfBills.get(0).getBillId(), response.getBody().get(0).getBillId());
            assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

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

            assertEquals(HttpStatus.FORBIDDEN, expectedException.getStatus());
            assertEquals(ResponseStatusException.class,expectedException.getClass());

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

            assertEquals(listOfBills.isEmpty(), newList.getBody().isEmpty());

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

        assertEquals(!listOfBills.isEmpty(), !response.getBody().isEmpty());
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
        assertEquals(HttpStatus.FORBIDDEN, expectedException.getStatus());


    }

    @Test
    public void testGetUnpaidBillClient204(){

        List<Bill> listOfBills = new ArrayList<>();

        when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("user").build());
        when(clientService.getByUsername("user")).thenReturn(Client.builder().id(1).build());
        when(billService.getClientUnpaid(1)).thenReturn(listOfBills);

        ResponseEntity<List<Bill>> response = clientController.getUnpaidBillClient(auth,1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(listOfBills.isEmpty(), response.getBody().isEmpty());
    }
    @SneakyThrows
    @Test
    public void getClientConsumptionHappyPath(){
        ClientConsumption clientConsumption =mock(ClientConsumption.class);
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("user").build());
        when(clientService.getByUsername("user")).thenReturn(Client.builder().id(1).build());
        when(readingService.getClientConsumption(clientId,from,to)).thenReturn(clientConsumption);

        ResponseEntity response = clientController.getClientConsumption(auth,clientId,from,to);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        assertEquals(clientConsumption,response.getBody());


    }

    @SneakyThrows
    @Test
    public void getClientConsumption403(){
        ClientConsumption clientConsumption =mock(ClientConsumption.class);
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(UserDTO.builder().username("user").build());
        when(clientService.getByUsername("user")).thenReturn(Client.builder().id(2).build());

        try {
            clientController.getClientConsumption(auth,1,from,to);
        }catch(ResponseStatusException e){
            expectedException = e;
        }

        assertEquals(HttpStatus.FORBIDDEN, expectedException.getStatus());
    }
    @SneakyThrows
    @Test
    public void getClientReadingsByDateHappyPath(){
        Integer clientId = 1;
        Integer page = 1;
        Integer size = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        when(auth.getPrincipal()).thenReturn(aUserDTO());
        when(clientService.getByUsername(anyString())).thenReturn(aClient());
        when(readingService.getClientReadingsByDate(clientId,from,to,page,size)).thenReturn(List.of(aReading()));

        ResponseEntity response = clientController.getClientReadingsByDate(auth,clientId,from,to,page,size);

        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assert.assertEquals(List.of(aReading()),response.getBody());
    }


    @SneakyThrows
    @Test
    public void getClientReadingsByDate204(){
        Integer clientId = 1;
        Integer page = 1;
        Integer size = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        when(auth.getPrincipal()).thenReturn(aUserDTO());
        when(clientService.getByUsername(anyString())).thenReturn(aClient());
        when(readingService.getClientReadingsByDate(clientId,from,to,page,size)).thenReturn(anEmptyList());

        ResponseEntity response = clientController.getClientReadingsByDate(auth,clientId,from,to,page,size);

        Assert.assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
        Assert.assertEquals(anEmptyList(),response.getBody());
    }

    @SneakyThrows
    @Test
    public void getClientReadingsByDate403(){
        Integer clientId = 1;
        Integer page = 1;
        Integer size = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        when(auth.getPrincipal()).thenReturn(aUserDTO());
        when(clientService.getByUsername(anyString())).thenReturn(Client.builder().id(2).build());

        try {
            clientController.getClientReadingsByDate(auth,1,from,to,page,size);
        }catch(ResponseStatusException e){
            expectedException = e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN,expectedException.getStatus());

    }
}

