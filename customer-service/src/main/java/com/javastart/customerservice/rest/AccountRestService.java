package com.javastart.customerservice.rest;

import com.javastart.customerservice.controller.dto.AccountResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountRestService {

    @Value("${internal.account.service.url}")
    private String accountServiceUrl;

    private final RestTemplate restTemplate;

    public AccountRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AccountResponseDTO getAccountById(Long accountId) {
        ResponseEntity<AccountResponseDTO> accountResponse = restTemplate
                .getForEntity(accountServiceUrl.concat(String.valueOf(accountId)),
                        AccountResponseDTO.class);
        return accountResponse.getBody();
    }
}
