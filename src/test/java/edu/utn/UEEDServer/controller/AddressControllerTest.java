package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.AbstractController;
import edu.utn.UEEDServer.service.AddressService;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static edu.utn.UEEDServer.utils.TestUtils.aAddressJSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AddressController.class)
public class AddressControllerTest  extends AbstractController {

    @MockBean
    private AddressService addressService;


    @Test
    public void getAll() throws Exception{
        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .get("/address")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getById() throws Exception {

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .get("/address/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void add() throws Exception {

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .post("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aAddressJSON()))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void addBadRequest() throws Exception {

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .post("/address")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertEquals(HttpStatus.BAD_REQUEST.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test void delete() throws Exception{

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .delete("/address/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());


    }

    @Test
    public void update() throws Exception {

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .put("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aAddressJSON()))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void updateBadRequest() throws Exception {

        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .put("/address")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertEquals(HttpStatus.BAD_REQUEST.value(),resultActions.andReturn().getResponse().getStatus());
    }



}
