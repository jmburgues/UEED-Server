package edu.utn.UEEDServer.controller;


import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.ReadingDTO;
import edu.utn.UEEDServer.service.MeterService;
import edu.utn.UEEDServer.service.ReadingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static edu.utn.UEEDServer.utils.TestUtils.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeterControllerTest {


    MeterService meterService;

    ReadingService readingService;

    ConversionService conversionService;

    private MeterController meterController;

    @Before
    public void setUp(){

        meterService=mock(MeterService.class);
        conversionService=mock(ConversionService.class);
        readingService=mock(ReadingService.class);
        this.meterController = new MeterController(meterService,conversionService,readingService);
    }

    @Test
    public void addReadingTest_200(){

        //given
        ReadingDTO readingDTO = aReadingDTO();
        String id = "aaa111";
        Meter meter = aMeter();

        when(meterService.getById(aReadingDTO().getMeterSerialNumber())).thenReturn(meter);
        when(conversionService.convert(aReadingDTO(), Reading.class)).thenReturn(aReading());
        ResponseEntity response = meterController.addReading(aReadingDTO());
        Assert.assertEquals(meter.getPassword(),readingDTO.getPassword());

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCode().value());

    }

    @Test
    public void addReadingTest_401() {
        ReadingDTO readingDTO = aReadingDTO();
        String id = "aaa111";
        ResponseStatusException expectedException = null;
        Meter meter = Meter.builder().password("4568").build();

        when(meterService.getById(aReadingDTO().getMeterSerialNumber())).thenReturn(meter);


        try {
            meterController.addReading(readingDTO);
        } catch (ResponseStatusException e) {
            expectedException = e;
        }

        /*Assert.assertThrows(ResponseStatusException.class,()->meterController.addReading(aReadingDTO()));*/
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, expectedException.getStatus());
        Assert.assertEquals(ResponseStatusException.class, expectedException.getClass());


    }
}
