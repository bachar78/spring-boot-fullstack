package com.bachar.auth;

import com.bachar.auth.AuthenticationRequest;
import com.bachar.auth.AuthenticationResponse;
import com.bachar.customer.Customer;
import com.bachar.customer.CustomerDTO;
import com.bachar.customer.CustomerDTOMapper;
import com.bachar.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final CustomerDTOMapper customerDTOMapper;

    public AuthenticationService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, CustomerDTOMapper customerDTOMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customerDTOMapper = customerDTOMapper;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Customer principals = (Customer) authentication.getPrincipal();
        CustomerDTO customerDTO = customerDTOMapper.apply(principals);
        String token = jwtUtil.issueToken(customerDTO.email(), customerDTO.roles());
        return new AuthenticationResponse(token, customerDTO);
    }

}
