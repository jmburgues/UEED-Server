package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.repository.ClientRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
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
    @Test
    public void updateTest_TRUE(){

        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(aClient()));
        when(clientRepository.save(aClient())).thenReturn(aClient());

        Boolean updatedClient = clientService.update(aClient());
        Assert.assertEquals(Boolean.TRUE,updatedClient);
    }
    @Test
    public void updateTest_FALSE(){

        when(clientRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(clientRepository.save(aClient())).thenReturn(aClient());

        Boolean updatedClient = clientService.update(aClient());
        Assert.assertEquals(Boolean.FALSE,updatedClient);
    }
    @Test
    public void getAllTest(){

        when(clientRepository.findAll()).thenReturn(List.of(aClient()));

        List<Client>actualList = clientService.getAll();

        Assert.assertEquals(List.of(aClient()),actualList);
    }

    @Test
    public void getByUsernameTest() {
        String username = "username";
        when(clientRepository.getByUsername(username)).thenReturn(aClient());

        Client actualClient = clientService.getByUsername(username);

        Assert.assertEquals(aClient(),actualClient);
    }
}
