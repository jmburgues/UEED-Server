package edu.utn.UEEDServer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.model.dto.LoginRequestDto;
import edu.utn.UEEDServer.model.dto.LoginResponseDto;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.UserService;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static edu.utn.UEEDServer.utils.TestUtils.aUser;
import static edu.utn.UEEDServer.utils.TestUtils.aUserDTO;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {


    private UserService userService;

    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    private UserController userController;

    private LoginRequestDto loginRequestDto;

    @Before
    public void setUp(){
        userService=mock(UserService.class);
        objectMapper=mock(ObjectMapper.class);
        modelMapper=mock(ModelMapper.class);

        this.userController = new UserController(userService,objectMapper,modelMapper);
        this.loginRequestDto = LoginRequestDto
                .builder().username("user")
                .password("1234")
                .build();
    }

    @SneakyThrows
    @Test
    public void loginTest_200(){

        //given
        User user = aUser();
        UserDTO userDTO = aUserDTO();
        ResponseEntity<LoginResponseDto>response= null;


        when(userService.findByUserAndPass(loginRequestDto.getUsername(),loginRequestDto.getPassword()))
                .thenReturn(user);
        when(modelMapper.map(user,UserDTO.class)).thenReturn(userDTO);


        response = userController.login(loginRequestDto);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCode().value());
    }

    @SneakyThrows
    @Test
    public void loginTest_401() {

        ResponseStatusException expectedException = null;

        when(userService.findByUserAndPass(loginRequestDto.getUsername(), loginRequestDto.getPassword()))
                .thenReturn(null);

        try {
            userController.login(loginRequestDto);
        } catch (ResponseStatusException e) {
            expectedException = e;
        }

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), expectedException.getStatus().value());
    }

}
