package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static edu.utn.UEEDServer.utils.TestUtils.aUser;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    @Before
    public void setUp(){
        initMocks(this);
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
