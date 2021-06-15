package edu.utn.UEEDServer.controller;


import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.AddressDTO;
import edu.utn.UEEDServer.model.dto.MeterDTO;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.model.projections.ConsumersDTO;
import edu.utn.UEEDServer.service.*;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.*;
import static org.mockito.Mockito.*;

public class BackOfficeControllerTest {



    private RateService rateService;

    private AddressService addressService;

    private MeterService meterService;

    private BillService billService;

    private ReadingService readingService;

    private ModelMapper modelMapper;

    private BackofficeController backofficeController;


    Authentication auth;

    private ResponseStatusException expectedException;

    private UserDTO employee;

    private Integer page;
    private Integer size;



    @Before
    public void setUp(){
        rateService=mock(RateService.class);
        addressService=mock(AddressService.class);
        meterService=mock(MeterService.class);
        billService=mock(BillService.class);
        readingService=mock(ReadingService.class);
        modelMapper=mock(ModelMapper.class);
        auth=mock(Authentication.class);

        backofficeController = new BackofficeController(
                rateService,addressService,meterService,billService,readingService,modelMapper);
        employee = anEmployee();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        page = 1;
        size = 1;
    }
    @Test
    public void getAllRateTest_200(){

        auth = mock(Authentication.class);
        List<Rate> rates = new ArrayList<>();
        rates.add(aRate());

        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.getAll()).thenReturn(List.of(aRate()));

        ResponseEntity<List<Rate>> actualList=backofficeController.getAllRate(auth);

        Assert.assertEquals(actualList.getBody().size(),rates.size());


    }

    @Test
    public void getAllRate_403(){

        UserDTO userDTO = aUserDTO();
        when(auth.getPrincipal()).thenReturn(userDTO);

        try {
            backofficeController.getAllRate(auth);
        }    catch(ResponseStatusException e)
        {
            expectedException = e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void getByIdRateTest_200(){
        //given
        Integer rateId = 1;
        Rate rate = aRate();

        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.getById(rateId)).thenReturn(rate);

        ResponseEntity<Rate> response = backofficeController.getByIdRate(auth,rateId);

        Assert.assertEquals(rate,response.getBody());
        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    public void getByIdRateTest_403(){

        Integer rateId = 1;
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.getByIdRate(auth,rateId);
        }catch (ResponseStatusException ex){

            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }

    @Test
    public void addRateTest_201(){

        Rate rate = aRate();

        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.add(rate)).thenReturn(rate);

        ResponseEntity response = backofficeController.addRate(auth,rate);

        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());


    }

    @Test
    public void addRateTest_403(){
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.addRate(auth,aRate());
        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void updateRateTest_200()
    {
        Rate r = aRate();
        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.update(r)).thenReturn(true);

        ResponseEntity responseEntity = backofficeController.updateRate(auth,r);

        Assert.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());


    }

    @Test
    public void updateRateTest_201(){

        Rate rate = aRate();
        when(auth.getPrincipal()).thenReturn(employee);

        when(rateService.update(rate)).thenReturn(false);

        ResponseEntity response = backofficeController.updateRate(auth,rate);

        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());

    }

    @Test
    public void updateRateTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try{
            backofficeController.updateRate(auth,aRate());
        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void deleteRateTest_200(){

        when(auth.getPrincipal()).thenReturn(employee);

         doNothing().when(rateService).delete(1);

         backofficeController.deleteRate(auth,1);

         verify(rateService,times(1)).delete(1);

    }

    @Test
    public void deleteRateTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.deleteRate(auth,1);
        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }

    @Test
    public void getAllAddressTest_200(){

        List<Address>addresses = List.of(anAddress());

        when(auth.getPrincipal()).thenReturn(employee);
        when(addressService.getAll(page,size)).thenReturn(addresses);

        ResponseEntity<List<Address>>actual=backofficeController.getAllAddress(auth,page,size);

        Assert.assertEquals(addresses.size(),actual.getBody().size());
    }

    @Test
    public void getAllAddressTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try{
            backofficeController.getAllAddress(auth,page,size);
        }catch (ResponseStatusException e){
            expectedException = e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void getByIdAddress_200(){

        Integer addressId = 1;
        Address address = anAddress();
        when(auth.getPrincipal()).thenReturn(employee);
        when(addressService.getById(addressId)).thenReturn(address);

        ResponseEntity<Address>response = backofficeController.getByIdAddress(auth,addressId);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(address,response.getBody());

    }

    @Test
    public void getByIdAddress_403(){
        Integer addressId = 1;
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.getByIdAddress(auth,addressId);

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void addAddressTest_201(){

        //given
        AddressDTO addressDTO = anAddressDTO();
        Address address = anAddress();
        Integer clientId=1;
        Integer rateId = 1;

        when(auth.getPrincipal()).thenReturn(employee);
        when(modelMapper.map(addressDTO,Address.class)).thenReturn(address);
        when(addressService.add(clientId,rateId,address)).thenReturn(address);

        ResponseEntity response =backofficeController.addAddress(auth,addressDTO);


        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());

    }

    @Test
    public void addAddressTest_403(){


        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.addAddress(auth,anAddressDTO());

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void updateAddressTest_200(){
        //given
        AddressDTO addressDTO = anAddressDTO();

        when(auth.getPrincipal()).thenReturn(employee);
        when(modelMapper.map(addressDTO,Address.class)).thenReturn(anAddress());
        when(addressService.update(anAddress())).thenReturn(true);


        ResponseEntity<Address> response = backofficeController.updateAddress(auth,addressDTO);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

    }
    @Test
    public void updateAddressTest_201(){
        //given
        AddressDTO addressDTO = anAddressDTO();

        when(auth.getPrincipal()).thenReturn(employee);
        when(modelMapper.map(addressDTO,Address.class)).thenReturn(anAddress());
        when(addressService.update(anAddress())).thenReturn(false);

        ResponseEntity<Address>response = backofficeController.updateAddress(auth,addressDTO);

        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());

    }

    @Test
    public void updateAddressTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.updateAddress(auth,anAddressDTO());

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }
    @Test
    public void deleteAddressTest_200(){
        //given
        Integer addressId =1;
        when(auth.getPrincipal()).thenReturn(employee);
        doNothing().when(addressService).delete(addressId);

        backofficeController.deleteAddress(auth,addressId);

        verify(addressService,times(1)).delete(addressId);
    }

    @Test
    public void deleteAddressTest_403(){
        Integer addressId = 1;
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.deleteAddress(auth,addressId);

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void getAllMeterTest_200(){

        when(auth.getPrincipal()).thenReturn(employee);
        when(meterService.getAll(page,size)).thenReturn(List.of(aMeter()));

        ResponseEntity<List<Meter>>response =backofficeController.getAllMeter(auth,page,size);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(List.of(aMeter()).size(),response.getBody().size());

    }

    @Test
    public void getAllMeterTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.getAllMeter(auth,page,size);
        }catch (ResponseStatusException ex){
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }

    @Test
    public void getAllMeterTest_204(){

        when(auth.getPrincipal()).thenReturn(employee);
        when(meterService.getAll(page,size)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Meter>>response = backofficeController.getAllMeter(auth,page,size);

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCodeValue());
        Assert.assertEquals(anEmptyList(),response.getBody());
    }

    @Test
    public void getByIdMeterTest_200(){
        String meterId = "aaa111";

        when(auth.getPrincipal()).thenReturn(employee);
        when(meterService.getById(meterId)).thenReturn(aMeter());

        ResponseEntity<Meter>response = backofficeController.getByIdMeter(auth,meterId);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(aMeter(),response.getBody());
    }

    @Test
    public void getByIdMeterTest_403(){
        String meterId = "aaa111";

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.getByIdMeter(auth,meterId);
        }catch (ResponseStatusException ex){
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void addMeterTest_201(){
        Meter meter = aMeter();
        MeterDTO meterDTO = aMeterDTO();
        when(auth.getPrincipal()).thenReturn(employee);
        when(meterService.add(aMeter(),1,1)).thenReturn(meter);
        when(modelMapper.map(meterDTO,Meter.class)).thenReturn(aMeter());

        ResponseEntity response = backofficeController.addMeter(auth,meterDTO);

        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());
    }

    @Test
    public void addMeterTest_403(){
        Meter meter = aMeter();
        MeterDTO meterDTO = aMeterDTO();
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.addMeter(auth,meterDTO);
        }catch (ResponseStatusException ex){
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void  updateMeterTest_200(){

        Meter meter = aMeter();
        when(auth.getPrincipal()).thenReturn(employee);
        when(meterService.update(meter)).thenReturn(true);

        ResponseEntity responseEntity = backofficeController.updateMeter(auth,meter);

        Assert.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());

    }
    @Test
    public void  updateMeterTest_201(){

        Meter meter = aMeter();
        when(auth.getPrincipal()).thenReturn(employee);
        when(meterService.update(meter)).thenReturn(false);

        ResponseEntity responseEntity = backofficeController.updateMeter(auth,meter);

        Assert.assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCodeValue());

    }
    @Test
    public void  updateMeterTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.updateMeter(auth,aMeter());
        }catch (ResponseStatusException ex){
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void deleteMeterTest_200(){
        String meterId = "aaa111";

        when(auth.getPrincipal()).thenReturn(employee);
        doNothing().when(meterService).delete(meterId);

        backofficeController.deleteMeter(auth,meterId);

        verify(meterService,times(1)).delete(meterId);

    }

    @Test
    public void deleteMeterTest_403(){
        String meterId = "aaa111";
        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try{
            backofficeController.deleteMeter(auth,meterId);
        }catch (ResponseStatusException ex){
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @SneakyThrows
    @Test
    public void filterByDateTest_200(){

        //given
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(employee);
        when(billService.filterByClientAndDate(clientId,from,to)).thenReturn(List.of(aBill()));

        ResponseEntity<List<Bill>>response = backofficeController.filterByDate(auth,clientId,from,to);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(List.of(aBill()),response.getBody());
    }

    @SneakyThrows
    @Test
    public void filterByDateTest_403() {

        //given
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try{
            backofficeController.filterByDate(auth,clientId,from,to);
        }catch (ResponseStatusException ex){
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @SneakyThrows
    @Test
    public void filterByDateTest_204(){

        //given
        Integer clientId = 1;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(employee);
        when(billService.filterByClientAndDate(clientId,from,to)).thenReturn(anEmptyList());

        ResponseEntity<List<Bill>>response = backofficeController.filterByDate(auth,clientId,from,to);

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCodeValue());
        Assert.assertEquals(new ArrayList<>(),response.getBody());
    }

    @Test
    public void getUnpaidBillClientTest_200(){
        //given
        Integer clientId = 1;
        when(auth.getPrincipal()).thenReturn(employee);
        when(billService.getClientUnpaid(clientId)).thenReturn(List.of(aBill()));

        ResponseEntity<List<Bill>>response = backofficeController.getUnpaidBillClient(auth,clientId);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(List.of(aBill()),response.getBody());
    }

    @Test
    public void getUnpaidBillClientTest_204(){
        //given
        Integer clientId = 1;
        when(auth.getPrincipal()).thenReturn(employee);
        when(billService.getClientUnpaid(clientId)).thenReturn(anEmptyList());

        ResponseEntity<List<Bill>>response = backofficeController.getUnpaidBillClient(auth,clientId);

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCodeValue());
        Assert.assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void getUnpaidBillClientTest_403() {
        //given
        Integer clientId = 1;

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try {
            backofficeController.getUnpaidBillClient(auth, clientId);
        } catch (ResponseStatusException ex) {
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), expectedException.getStatus().value());

    }

    @Test
    public void getUnpaidBillAddressTest_200(){
        //given
        Integer addressId = 1;
        when(auth.getPrincipal()).thenReturn(employee);
        when(billService.getAddressUnpaid(addressId)).thenReturn(List.of(aBill()));

        ResponseEntity<List<Bill>>response = backofficeController.getUnpaidBillAddress(auth,addressId);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(List.of(aBill()),response.getBody());
    }

    @Test
    public void getUnpaidBillAddressTest_204(){
        //given
        Integer addressId = 1;
        when(auth.getPrincipal()).thenReturn(employee);
        when(billService.getAddressUnpaid(addressId)).thenReturn(anEmptyList());

        ResponseEntity<List<Bill>>response = backofficeController.getUnpaidBillAddress(auth,addressId);

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCodeValue());
        Assert.assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void getUnpaidBillAddressTest_403() {
        //given
        Integer addressId = 1;

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try {
            backofficeController.getUnpaidBillAddress(auth, addressId);
        } catch (ResponseStatusException ex) {
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), expectedException.getStatus().value());

    }

    @SneakyThrows
    @Test
    public void getAddressReadingsTest_200(){
        //given
        Integer addressId = 1;
        Integer page = 0;
        Integer size = 10;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(employee);
        when(readingService.getAddressReadingsByDate(addressId,from,to,page,size)).thenReturn(List.of(aReading()));

        ResponseEntity<List<Reading>>response = backofficeController.getAddressReadings(auth,addressId,from,to,page,size);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(List.of(aReading()),response.getBody());
    }

    @SneakyThrows
    @Test
    public void getAddressReadingsTest_204(){
        //given
        Integer addressId = 1;
        Integer page = 0;
        Integer size = 10;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(employee);
        when(readingService.getAddressReadingsByDate(addressId,from,to,page,size)).thenReturn(anEmptyList());

        ResponseEntity<List<Reading>>response = backofficeController.getAddressReadings(auth,addressId,from,to,page,size);

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCodeValue());
        Assert.assertTrue(response.getBody().isEmpty());
    }

    @SneakyThrows
    @Test
    public void getAddressReadingsTest_403(){
        //given
        Integer addressId = 1;
        Integer page = 0;
        Integer size = 10;
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try {
            backofficeController.getAddressReadings(auth,addressId,from,to,page,size);
        } catch (ResponseStatusException ex) {
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), expectedException.getStatus().value());
    }

    @SneakyThrows
    @Test
    public void getTopConsumersTest_200(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        ConsumersDTO consumersDTO = mock(ConsumersDTO.class);

        when(auth.getPrincipal()).thenReturn(employee);
        when(readingService.getTopConsumers(from,to)).thenReturn(List.of(consumersDTO));

        ResponseEntity<List<ConsumersDTO>>response = backofficeController.getTopConsumers(auth,from,to);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

    }
    @SneakyThrows
    @Test
    public void getTopConsumersTest_204(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");
        ConsumersDTO consumersDTO = mock(ConsumersDTO.class);

        when(auth.getPrincipal()).thenReturn(employee);
        when(readingService.getTopConsumers(from,to)).thenReturn(anEmptyList());

        ResponseEntity<List<ConsumersDTO>>response = backofficeController.getTopConsumers(auth,from,to);

        Assert.assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCodeValue());
        Assert.assertTrue(response.getBody().isEmpty());

    }

    @SneakyThrows
    @Test
    public void getTopConsumersTest_404(){
        Date from = new SimpleDateFormat("yyyy-MM").parse("2021-06");
        Date to = new SimpleDateFormat("yyyy-MM").parse("2021-07");


        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.getTopConsumers(auth,from,to);
        } catch (ResponseStatusException ex) {
            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), expectedException.getStatus().value());
    }
}


