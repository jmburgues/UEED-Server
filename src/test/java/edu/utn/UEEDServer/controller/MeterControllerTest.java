package edu.utn.UEEDServer.controller;


import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.dto.ReadingDTO;
import edu.utn.UEEDServer.service.MeterService;
import edu.utn.UEEDServer.service.ReadingService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static edu.utn.UEEDServer.utils.TestUtils.aMeter;
import static edu.utn.UEEDServer.utils.TestUtils.aReadingDTO;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MeterControllerTest {

    @Mock
    MeterService meterService;
    @Mock
    ReadingService readingService;
    @Mock
    ConversionService conversionService;

    private MeterController meterController;

    @BeforeEach
    public void setUp(){

        initMocks(this);
        this.meterController = new MeterController(meterService,conversionService,readingService);
    }

    @Test
    public void addReadingTest_200(){

        //given
        ReadingDTO readingDTO = aReadingDTO();
        String id = "aaa111";
        Meter meter = aMeter();

        when(meterService.getById(aReadingDTO().getMeterSerialNumber())).thenReturn(meter);

        ResponseEntity response = meterController.addReading(aReadingDTO());
        Assert.assertEquals(meter.getPassword(),readingDTO.getPassword());

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCode().value());

    }

    @Test
    public void addReadingTest_401(){
        ReadingDTO readingDTO = aReadingDTO();
        String id = "aaa111";
        ResponseStatusException expectedException = null;
        Meter meter = Meter.builder().password("4568").build();

        when(meterService.getById(aReadingDTO().getMeterSerialNumber())).thenReturn(meter);



        try {
            meterController.addReading(readingDTO);
        }catch(ResponseStatusException e){
            expectedException = e;
        }

        /*Assert.assertThrows(ResponseStatusException.class,()->meterController.addReading(aReadingDTO()));*/
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, expectedException.getStatus());
        Assert.assertEquals(ResponseStatusException.class,expectedException.getClass());


    }
}
