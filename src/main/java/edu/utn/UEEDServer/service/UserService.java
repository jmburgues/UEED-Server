package edu.utn.UEEDServer.service;

import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findByUserAndPass(String username, String password){
        return userRepository.findByUserAndPass(username,password);
    }
}
