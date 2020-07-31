package com.javastart.customerservice.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.customerservice.controller.dto.*;
import com.javastart.customerservice.customer.controller.dto.CustomerRequestDTO;
import com.javastart.customerservice.customer.controller.dto.CustomerResponseDTO;
import com.javastart.customerservice.rest.BillRestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class CustomerService {


    @Value("${internal.account.service.url}")
    private String accountServiceUrl;

    @Value("${internal.bill.service.url}")
    private String billServiceUrl;

    private final RestTemplate restTemplate;

    private final BillRestService billRestService;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CustomerService(RestTemplate restTemplate, BillRestService billRestService, RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.billRestService = billRestService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public CustomerResponseDTO getCustomerByEmail(String email) {
        StringJoiner accountUrlBuilder = new StringJoiner("");
        accountUrlBuilder.add(accountServiceUrl).add("email/").add(email);
        ResponseEntity<AccountResponseDTO> accountResponse = restTemplate.getForEntity(accountUrlBuilder.toString(),
                AccountResponseDTO.class);
        AccountResponseDTO accountDto = accountResponse.getBody();
        Long accountId = accountDto.getAccountId();
        List<BillResponseDTO> billDTOs = billRestService.getBillsByAccountId(accountId);
        return new CustomerResponseDTO(accountDto, billDTOs);
    }

    public CustomerResponseDTO getCustomerByAccountId(Long id) {
        StringJoiner accountUrlBuilder = new StringJoiner("");
        accountUrlBuilder.add(accountServiceUrl).add(String.valueOf(id));
        ResponseEntity<AccountResponseDTO> accountResponse = restTemplate.getForEntity(accountUrlBuilder.toString(),
                AccountResponseDTO.class);
        AccountResponseDTO accountDto = accountResponse.getBody();
        List<BillResponseDTO> billDTOs = billRestService.getBillsByAccountId(id);
        return new CustomerResponseDTO(accountDto, billDTOs);
    }

    public String createCustomer(CustomerRequestDTO customerRequestDTO) {
        AccountRequestDTO accountRequestDto = customerRequestDTO.getAccountRequestDTO();
        ResponseEntity<Long> accountResponseId = restTemplate
                .postForEntity(accountServiceUrl, accountRequestDto, Long.class);
        Long accountId = accountResponseId.getBody();
        List<BillDTO> billRequestBody = customerRequestDTO.getBillRequestDTO().stream()
                .map(request -> new BillDTO(accountId, request.getIsDefault(), request.getAmount(),
                        request.getOverdraftEnabled()))
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder(billServiceUrl);
        sb.append("/list");
        ResponseEntity<Long[]> billsResponse = restTemplate.postForEntity(sb.toString(), billRequestBody,
                Long[].class);
        List<Long> billIds = Arrays.asList(Objects.requireNonNull(billsResponse.getBody()));
        AccountDTO accountDTO = new AccountDTO(accountRequestDto.getName(), accountRequestDto.getEmail(),
                accountRequestDto.getPhone(), billIds);
        AccountMessageResponseDTO accountMessageDTO = new AccountMessageResponseDTO(accountRequestDto.getName(),
                accountRequestDto.getEmail(), accountRequestDto.getPhone());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend("js.account.notify.exchange",
                    "js.account", objectMapper.writeValueAsString(accountMessageDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        restTemplate.put(accountServiceUrl.concat("/").concat(String.valueOf(accountId)), accountDTO);
        return "Success";
    }
}
