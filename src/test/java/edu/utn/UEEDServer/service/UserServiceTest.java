package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static edu.utn.UEEDServer.utils.TestUtils.aUser;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {


    UserRepository userRepository;

    UserService userService;

    @Before
    public void setUp(){
        userRepository=mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    public void findByUserAndPassTest(){
        String username = "user";
        String password = "pass";

        when(userRepository.findByUserAndPass(username,password)).thenReturn(aUser());

        User user = userService.findByUserAndPass(username,password);

        Assert.assertEquals(aUser(),user);
    }

}
