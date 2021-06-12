package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.exceptions.IDnotFoundException;
import edu.utn.UEEDServer.model.Rate;
import edu.utn.UEEDServer.repository.RateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static edu.utn.UEEDServer.utils.TestUtils.aRate;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RateServiceTest {

    @Mock
    RateRepository rateRepository;

    RateService rateService;

    @Before
    public void setUp(){

        initMocks(this);
        this.rateService = new RateService(rateRepository);
    }

    @Test
    public void getAllTest(){

        when(rateRepository.findAll()).thenReturn(List.of(aRate()));

        List<Rate>actualList = rateService.getAll();

        Assert.assertEquals(List.of(aRate()),actualList);
    }
    @Test
    public void getByIdTest(){
        when(rateRepository.findById(anyInt())).thenReturn(Optional.of(aRate()));

        Rate actualRate = rateService.getById(anyInt());

        Assert.assertEquals(aRate(),actualRate);
    }
    @Test
    public void getByIdTest_NotFound(){
        when(rateRepository.findById(anyInt())).thenReturn(Optional.empty());

        Assert.assertThrows(IDnotFoundException.class,()->rateService.getById(anyInt()));
    }
    @Test
    public void addTest(){

        when(rateRepository.findById(anyInt())).thenReturn(Optional.empty());
        when(rateRepository.save(aRate())).thenReturn(aRate());

        Rate actualRate = rateService.add(aRate());

        Assert.assertEquals(aRate(),actualRate);
    }
    @Test
    public void addTest_AlreadyExist(){

        when(rateRepository.findById(anyInt())).thenReturn(Optional.of(aRate()));
        Assert.assertThrows(IllegalArgumentException.class,()->rateService.add(aRate()));
    }
    @Test
    public void deleteTest(){
        when(rateRepository.findById(anyInt())).thenReturn(Optional.of(aRate()));
        rateService.delete(anyInt());
        verify(rateRepository,times(1)).deleteById(anyInt());
    }
    @Test
    public void deleteTest_NotFound(){
        when(rateRepository.findById(anyInt())).thenReturn(Optional.empty());
        Assert.assertThrows(IDnotFoundException.class,()->rateService.delete(anyInt()));
    }
}
