package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.projections.ClientConsumption;
import edu.utn.UEEDServer.model.projections.ConsumersDTO;
import edu.utn.UEEDServer.repository.ReadingRepository;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.aReading;
import static edu.utn.UEEDServer.utils.TestUtils.anAddress;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReadingServiceTest {


    MeterService meterService;

    ReadingRepository readingRepository;

    private AddressService addressService;

    private ReadingService readingService;
    Integer page;
    Integer size;


    @Before
    public void setUp(){

        addressService=mock(AddressService.class);
        readingRepository= mock(ReadingRepository.class);
        meterService=mock(MeterService.class);
        this.readingService = new ReadingService(meterService, readingRepository, addressService);
        page = 1;
        size = 1;
    }

    @Test
    public void addTest(){
        when(readingRepository.save(aReading())).thenReturn(aReading());
        Reading actualReading = readingService.add(aReading());

        Assert.assertEquals(aReading(),actualReading);
    }
    @SneakyThrows
    @Test
    public void getAddressReadingsByDateTest(){
        //given
        Integer addressId =1;
        Integer page=0;
        Integer size=10;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(addressService.getById(addressId)).thenReturn(anAddress());
        when(readingRepository.getAddressReadingsByDate(addressId,from,to,page,size)).thenReturn(List.of(aReading()));

        List<Reading>readingList = readingService.getAddressReadingsByDate(addressId,from,to,page,size);

        Assert.assertEquals(List.of(aReading()),readingList);
    }
    @SneakyThrows
    @Test
    public void getAddressReadingsByDateTest_IllegalArguments(){
        Integer addressId = 1;
        Integer page = 0;
        Integer size = 10;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-04");

        Assert.assertThrows(IllegalArgumentException.class,
                ()->readingService.getAddressReadingsByDate(addressId,from,to,page,size));
    }
    @SneakyThrows
    @Test
    public void getClientReadingsByDateTest(){
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        when(readingRepository.getClientReadingsByDate(clientId, from, to, page, size)).thenReturn(List.of(aReading()));

        List<Reading>actualList = readingService.getClientReadingsByDate(clientId,from,to,page,size);

        Assert.assertEquals(List.of(aReading()),actualList);
    }
    @SneakyThrows
    @Test
    public void getClientReadingsByDateTest_IllegalArguments(){

        Integer clientId =1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-04");

        Assert.assertThrows(IllegalArgumentException.class,
                ()->readingService.getClientReadingsByDate(clientId,from,to,page,size));
    }

    @SneakyThrows
    @Test
    public void getClientConsumptionTest(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        Integer clientId = 1;
        ClientConsumption clientConsumption = mock(ClientConsumption.class);
        when(readingRepository.getClientConsumption(clientId,from,to)).thenReturn(clientConsumption);

        ClientConsumption actualClientConsumption = readingService.getClientConsumption(clientId, from, to);

        Assert.assertEquals(clientConsumption,actualClientConsumption);
    }
    @SneakyThrows
    @Test
    public void getClientConsumptionTest_IllegalArguments(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-04");
        Integer clientId = 1;


        Assert.assertThrows(IllegalArgumentException.class,
                ()->readingService.getClientConsumption(clientId,from,to));
    }
    @SneakyThrows
    @Test
    public void getTopConsumersTest(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
       ConsumersDTO consumersDTO = mock(ConsumersDTO.class);

       when(readingRepository.getTopConsumers(from,to)).thenReturn(List.of(consumersDTO));

        List<ConsumersDTO> consumersDTOList = readingService.getTopConsumers(from, to);

        Assert.assertEquals(List.of(consumersDTO),consumersDTOList);
    }
    @SneakyThrows
    @Test
    public void getTopConsumersTest_IllegalArgument(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-02");

        Assert.assertThrows(IllegalArgumentException.class,
                ()->readingService.getTopConsumers(from,to));
    }



}
