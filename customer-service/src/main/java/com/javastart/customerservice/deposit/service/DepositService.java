package com.javastart.customerservice.deposit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.javastart.customerservice.controller.dto.AccountResponseDTO;
import com.javastart.customerservice.controller.dto.BillDTO;
import com.javastart.customerservice.controller.dto.BillResponseDTO;
import com.javastart.customerservice.deposit.controller.dto.DepositResponseDTO;
import com.javastart.customerservice.deposit.entity.Deposit;
import com.javastart.customerservice.deposit.repository.DepositRepository;
import com.javastart.customerservice.exceptions.DepositException;
import com.javastart.customerservice.rest.AccountRestService;
import com.javastart.customerservice.rest.BillRestService;
import com.javastart.customerservice.rest.CustomerRestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class DepositService {

    @Value("${internal.bill.service.url}")
    private String billServiceUrl;

    private final RestTemplate restTemplate;

    private final CustomerRestService customerRestService;

    private final AccountRestService accountRestService;

    private final DepositRepository depositRepository;

    private final BillRestService billRestService;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DepositService(RestTemplate restTemplate,
                          CustomerRestService customerRestService, AccountRestService accountRestService,
                          DepositRepository depositRepository, BillRestService billRestService,
                          RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.customerRestService = customerRestService;
        this.accountRestService = accountRestService;
        this.depositRepository = depositRepository;
        this.billRestService = billRestService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Deposit getById(Long depositId) {
        return depositRepository.findById(depositId).orElseThrow(()
                -> new DepositException("Unable to find deposit with id " + depositId));
    }

    public List<Deposit> getAllByAccountEmail(String accountEmail) {
        return depositRepository.findAllByEmail(accountEmail).orElseThrow(()
                -> new DepositException("There was no deposit on account with email " + accountEmail));
    }

    public List<Deposit> getAllByBillId(Long billId) {
        return depositRepository.findAllByBillId(billId).orElseThrow(()
                -> new DepositException("There was no deposit on bill with id " + billId));
    }

    @Transactional
    public Deposit deposit(Long accountId, Long billId, BigDecimal amount) {
        customerRestService.checkAmount(amount);
        if (billId != null) {
            BillResponseDTO billResponseDTO = billRestService.getBillById(billId);
            BillDTO billRequestDTO = BillDTO.builder()
                    .accountId(billResponseDTO.getAccountId())
                    .amount(billResponseDTO.getAmount().add(amount))
                    .overdraftEnabled(billResponseDTO.getOverdraftEnabled())
                    .build();
            AccountResponseDTO account = accountRestService.getAccountById(billResponseDTO.getAccountId());
            restTemplate.put(billServiceUrl.concat(billId.toString()), billRequestDTO);
            Deposit depositResponse = new Deposit(OffsetDateTime.now(), billId, account.getEmail(),
                    amount, billRequestDTO.getAmount());
            depositRepository.save(depositResponse);
            DepositResponseDTO depositResponseDTO = new DepositResponseDTO(depositResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rabbitTemplate.convertAndSend("js.deposit.notify.exchange",
                        "js.deposit", objectMapper.writeValueAsString(depositResponseDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return depositResponse;
        }
        BillResponseDTO defaultBill = billRestService.getDefaultBillByAccountId(accountId);
        BillDTO billRequestDTO = BillDTO.builder()
                .accountId(accountId)
                .isDefault(defaultBill.getIsDefault())
                .amount(defaultBill.getAmount().add(amount))
                .overdraftEnabled(defaultBill.getOverdraftEnabled())
                .build();
        StringBuilder putBillUrl = new StringBuilder(billServiceUrl);
        putBillUrl.append(defaultBill.getBillId());
        restTemplate.put(putBillUrl.toString(), billRequestDTO);
        AccountResponseDTO account = accountRestService.getAccountById(accountId);
        Deposit depositResponse = new Deposit(OffsetDateTime.now(), defaultBill.getBillId(), account.getEmail(),
                amount, billRequestDTO.getAmount());
        depositRepository.save(depositResponse);
        DepositResponseDTO depositResponseDTO = new DepositResponseDTO(depositResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rabbitTemplate.convertAndSend("js.deposit.notify.exchange",
                    "js.deposit", objectMapper.writeValueAsString(depositResponseDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return depositResponse;
    }
}
