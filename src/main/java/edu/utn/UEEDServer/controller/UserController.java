package edu.utn.UEEDServer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.model.dto.LoginRequestDto;
import edu.utn.UEEDServer.model.dto.LoginResponseDto;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static edu.utn.UEEDServer.utils.Constants.*;
import static edu.utn.UEEDServer.utils.Constants.JWT_SECRET;

@RestController
@RequestMapping("/login")
public class UserController {

    private UserService userService;
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) throws JsonProcessingException {
        User user = userService.findByUserAndPass(loginRequest.getUsername(), loginRequest.getPassword());
        if(user != null){
            UserDTO userDTO = modelMapper.map(user,UserDTO.class);
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(userDTO)).build());
        } else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password.");
        }
    }

    private String generateToken(UserDTO userDTO) throws JsonProcessingException {
        String authRole = userDTO.getEmployee() ? AUTH_EMPLOYEE : AUTH_CLIENT;

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authRole);
        String token = Jwts.builder()
                .setId("JWT")
                .setSubject(userDTO.getUsername())
                .claim("user",objectMapper.writeValueAsString(userDTO))
                .claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis() + 1800000 ))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();

        return token;
    }


}
