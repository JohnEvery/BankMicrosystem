package com.javastart.customerservice.withdraw.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javastart.customerservice.controller.dto.AccountResponseDTO;
import com.javastart.customerservice.controller.dto.BillDTO;
import com.javastart.customerservice.controller.dto.BillResponseDTO;
import com.javastart.customerservice.deposit.controller.dto.DepositResponseDTO;
import com.javastart.customerservice.deposit.entity.Deposit;
import com.javastart.customerservice.exceptions.WithdrawException;
import com.javastart.customerservice.exceptions.NotEnoughMoneyException;
import com.javastart.customerservice.rest.AccountRestService;
import com.javastart.customerservice.rest.BillRestService;
import com.javastart.customerservice.rest.CustomerRestService;
import com.javastart.customerservice.withdraw.controller.dto.WithdrawResponseDTO;
import com.javastart.customerservice.withdraw.entity.Withdraw;
import com.javastart.customerservice.withdraw.repository.WithdrawRepository;
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
public class WithdrawService {

    @Value("${internal.bill.service.url}")
    private String billServiceUrl;

    private final RestTemplate restTemplate;

    private final WithdrawRepository withdrawRepository;

    private final CustomerRestService customerRestService;

    private final BillRestService billRestService;

    private final AccountRestService accountRestService;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public WithdrawService(RestTemplate restTemplate, WithdrawRepository withdrawRepository, CustomerRestService customerRestService, BillRestService billRestService, AccountRestService accountRestService, RabbitTemplate rabbitTemplate) {
        this.restTemplate = restTemplate;
        this.withdrawRepository = withdrawRepository;
        this.customerRestService = customerRestService;
        this.billRestService = billRestService;
        this.accountRestService = accountRestService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Withdraw getWithdrawById(Long id) {
        return withdrawRepository.findById(id).orElseThrow(()
                -> new WithdrawException("Unable to find withdraw with id " + id));
    }

    public List<Withdraw> getAllByAccountEmail(String accountEmail) {
        return withdrawRepository.getByEmail(accountEmail).orElseThrow(()
                -> new WithdrawException("There was no deposit on account with email" + accountEmail));
    }

    public List<Withdraw> getAllByBillId(Long billId) {
        return withdrawRepository.getByBillId(billId).orElseThrow(()
                -> new WithdrawException("There was no withdraws on bill with id " + billId));
    }

    @Transactional
    public Withdraw withdraw(Long billId, Long accountId, BigDecimal amount) {
        customerRestService.checkAmount(amount);
        if (billId != null) {
            BillResponseDTO billResponseDTO = billRestService.getBillById(billId);
            AccountResponseDTO account = accountRestService.getAccountById(billResponseDTO.getAccountId());
            if (billResponseDTO.getAmount().compareTo(amount) >= 0) {
                BillDTO billRequestDTO = BillDTO.builder()
                        .accountId(account.getAccountId())
                        .amount(billResponseDTO.getAmount().subtract(amount))
                        .isDefault(billResponseDTO.getIsDefault())
                        .overdraftEnabled(billResponseDTO.getOverdraftEnabled())
                        .build();
                restTemplate.put(billServiceUrl.concat(billId.toString()), billRequestDTO);

                Withdraw withdraw = new Withdraw(OffsetDateTime.now(), billId, account.getEmail(),
                        amount, billRequestDTO.getAmount());
                withdrawRepository.save(withdraw);
                WithdrawResponseDTO withdrawResponseDTO = new WithdrawResponseDTO(withdraw);
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    rabbitTemplate.convertAndSend("js.withdraw.notify.exchange",
                            "js.withdraw", objectMapper.writeValueAsString(withdrawResponseDTO));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return withdraw;
            } else {
                throw new NotEnoughMoneyException("Not enough funds on bill with id " + billId);
            }
        }
        BillResponseDTO defaultBill = billRestService.getDefaultBillByAccountId(accountId);
        AccountResponseDTO account = accountRestService.getAccountById(accountId);
        if (defaultBill.getAmount().compareTo(amount) > 0) {
            BillDTO billRequestDTO = BillDTO.builder()
                    .accountId(account.getAccountId())
                    .amount(defaultBill.getAmount().subtract(amount))
                    .isDefault(defaultBill.getIsDefault())
                    .overdraftEnabled(defaultBill.getOverdraftEnabled())
                    .build();
            restTemplate.put(billServiceUrl.concat(defaultBill.getBillId().toString()), billRequestDTO);
            Withdraw withdrawResponse = new Withdraw(OffsetDateTime.now(), defaultBill.getBillId(), account.getEmail(),
                    amount, billRequestDTO.getAmount());
            withdrawRepository.save(withdrawResponse);
            WithdrawResponseDTO withdrawResponseDTO = new WithdrawResponseDTO(withdrawResponse);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                rabbitTemplate.convertAndSend("js.withdraw.notify.exchange",
                        "js.withdraw", objectMapper.writeValueAsString(withdrawResponseDTO));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return withdrawResponse;
        } else {
            throw new WithdrawException("Not enough funds on bill with id " + billId);
        }
    }
}
