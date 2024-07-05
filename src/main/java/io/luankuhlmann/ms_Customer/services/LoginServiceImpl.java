package io.luankuhlmann.ms_Customer.services;

import io.luankuhlmann.ms_Customer.dto.response.LoginResponseDTO;
import io.luankuhlmann.ms_Customer.dto.request.LoginRequestDTO;
import io.luankuhlmann.ms_Customer.exceptions.EntityNotFoundException;
import io.luankuhlmann.ms_Customer.exceptions.InvalidPasswordException;
import io.luankuhlmann.ms_Customer.infra.security.TokenService;
import io.luankuhlmann.ms_Customer.models.Customer;
import io.luankuhlmann.ms_Customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public LoginResponseDTO login(LoginRequestDTO body) {
        Customer customer = customerRepository.findByEmail(body.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (passwordEncoder.matches(body.password(), customer.getPassword())) {
            String token = tokenService.generateToken(customer);
            return new LoginResponseDTO(customer.getEmail(), token);
        } else {
            throw new InvalidPasswordException("Invalid password");
        }
    }
}