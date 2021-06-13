package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.repository.MeterRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static edu.utn.UEEDServer.utils.TestUtils.aMeter;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MeterServiceTest {

    @Mock
    MeterRepository meterRepository;

    MeterService meterService;

    Integer page;
    Integer size;

    @BeforeEach
    public void setUp(){
        initMocks(this);
        this.meterService = new MeterService(meterRepository);
        this.page = 1;
        this.size = 1;
    }

    @Test
    public void getAllTest(){

        when(meterRepository.findAllPagable(page,size)).thenReturn(List.of(aMeter()));

        List<Meter>actualList= meterService.getAll(page,size);

        Assert.assertEquals(List.of(aMeter()).size(),actualList.size());
        Assert.assertEquals(List.of(aMeter()),actualList);
    }

    @Test
    public void getByIdTest(){
        when(meterRepository.findById(anyString())).thenReturn(Optional.of(aMeter()));

        Meter actualMeter = meterService.getById(anyString());

        Assert.assertEquals(aMeter(),actualMeter);
    }
    @Test
    public void getByIdTest_NotFound(){
        when(meterRepository.findById(anyString())).thenReturn(Optional.empty());

        Assert.assertThrows(IDnotFoundException.class,()->meterService.getById(anyString()));
    }
    @Test
    public void addTest(){
        Meter meter = aMeter();
        String serialNumber = meter.getSerialNumber();
        when(meterRepository.existsById(serialNumber)).thenReturn(false);
        when(meterRepository.save(aMeter())).thenReturn(meter);

        Meter actualMeter = meterService.add(aMeter());

        Assert.assertEquals(meter,actualMeter);

    }

    @Test
    public void addTest_AlreadyExist(){
        when(meterRepository.existsById(anyString())).thenReturn(true);

        Assert.assertThrows(IllegalArgumentException.class,()->meterService.add(aMeter()));
    }
    @Test
    public void updateTest_TRUE(){
        when(meterRepository.findById(anyString())).thenReturn(Optional.of(aMeter()));
        Boolean actualMeter = meterService.update(aMeter());
        Assert.assertEquals(Boolean.TRUE,actualMeter);
    }

    @Test
    public void updateTest_FALSE(){
        when(meterRepository.findById(anyString())).thenReturn(Optional.empty());
        Boolean actualMeter = meterService.update(aMeter());
        Assert.assertEquals(Boolean.FALSE,actualMeter);
    }
    @Test
    public void deleteTest(){

        when(meterRepository.findById(anyString())).thenReturn(Optional.of(aMeter()));

        meterService.delete(anyString());
        verify(meterRepository,times(1)).deleteById(anyString());
    }
    @Test
    public void deleteTest_NotFound(){
        when(meterRepository.findById(anyString())).thenReturn(Optional.empty());

        Assert.assertThrows(IDnotFoundException.class,()->meterService.delete(anyString()));
    }
    @Test
    public void getByAddressIdTest(){
        when(meterRepository.findByAddressId(anyInt())).thenReturn(aMeter());

        Meter actualMeter = meterService.getByAddressId(anyInt());

        Assert.assertEquals(aMeter(),actualMeter);
    }
}
