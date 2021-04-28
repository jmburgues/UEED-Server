package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.repository.UserRepository;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class UserService {

    private static final String USER_PATH = "user";
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PostResponse add(User user) {

        User u = userRepository.save(user);

        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(USER_PATH,u.getUsername()))
                .build();

    }

    public List<User> getAll() {

        return userRepository.findAll();
    }

    public User getByUsername(String username)
    {
        return userRepository.findById(username).
                orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
}
