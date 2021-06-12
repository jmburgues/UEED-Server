package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.repository.AddressRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static edu.utn.UEEDServer.utils.TestUtils.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;
    @Mock
    ClientService clientService;
    @Mock
    RateService rateService;

    AddressService addressService;

    private Integer page;
    private  Integer size;

    @Before
    public void setUp(){
        initMocks(this);
        addressService = new AddressService(addressRepository,clientService,rateService);
        page = 1;
        size = 1;
    }

    @Test
    public void addTest(){  //todo deberia tirar un sql exception si el address ya existe
        /*
        *   MODIFICADO. Ahora verifica si existe por street y number y tira excepcion.
         */
        //given
        Integer clientId = 1;
        Integer rateId = 1;
        Address address = anAddress();

        when(rateService.getById(rateId)).thenReturn(aRate());
        when(clientService.getById(clientId)).thenReturn(aClient());
        when(addressRepository.save(address)).thenReturn(address);

        Address actualAddress = addressService.add(clientId,rateId,address);

        Assert.assertEquals(address,actualAddress);

    }
    @Test
    public void getAllTest(){

        List<Address> addressList=List.of(anAddress());

        when(addressRepository.findAllPageable(page,size)).thenReturn(addressList);

        List<Address>actualList = addressService.getAll(page,size);

        Assert.assertEquals(addressList.size(),actualList.size());
    }

    @Test
    public void getByIdTest(){
        Integer addressId = 1;

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(anAddress()));

        Address actualAddress = addressService.getById(addressId);

        Assert.assertEquals(anAddress(),actualAddress);
    }

    @Test
    public void getByIdTest_NotFound(){
        Integer addressId = 1;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        Assert.assertThrows(IDnotFoundException.class,()->addressService.getById(addressId));
    }
    @Test
    public void deleteTest(){
        Integer addressId = 1;
        when(addressRepository.existsById(addressId)).thenReturn(true);

        addressService.delete(addressId);

        verify(addressRepository,times(1)).deleteById(addressId);
    }
    @Test
    public void deleteTest_NotFound(){
        when(addressRepository.existsById(anyInt())).thenReturn(false);

        Assert.assertThrows(IDnotFoundException.class,()->addressService.delete(anyInt()));
    }
    @Test
    public void updateTest(){
        //given
        Address address = anAddress();

        when(addressRepository.save(anAddress())).thenReturn(address);
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(address));


        Address actual = addressService.update(anAddress());
        Assert.assertEquals(address,actual);
    }

}
