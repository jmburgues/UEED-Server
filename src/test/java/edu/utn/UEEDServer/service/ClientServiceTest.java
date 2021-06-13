package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.repository.ClientRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static edu.utn.UEEDServer.utils.TestUtils.aClient;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest {


    ClientRepository clientRepository;

    ClientService clientService;

    @Before
    public void setUp(){

        clientRepository = mock(ClientRepository.class);
        this.clientService = new ClientService(clientRepository);
    }

    @Test
    public void getByIdTest(){

        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(aClient()));

        Client actualClient = clientService.getById(anyInt());

        Assert.assertEquals(aClient(),actualClient);
    }

    @Test
    public void getByIdTest_NotFound(){

        when(clientRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assert.assertThrows(IDnotFoundException.class,()->clientService.getById(anyInt()));
    }
}
