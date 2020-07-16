package com.javastart.customerservice.withdraw.service;

import com.javastart.customerservice.controller.dto.AccountResponseDTO;
import com.javastart.customerservice.controller.dto.BillDTO;
import com.javastart.customerservice.controller.dto.BillResponseDTO;
import com.javastart.customerservice.exceptions.WithdrawException;
import com.javastart.customerservice.exceptions.NotEnoughMoneyException;
import com.javastart.customerservice.rest.AccountRestService;
import com.javastart.customerservice.rest.BillRestService;
import com.javastart.customerservice.rest.CustomerRestService;
import com.javastart.customerservice.withdraw.entity.Withdraw;
import com.javastart.customerservice.withdraw.repository.WithdrawRepository;
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

    @Autowired
    public WithdrawService(RestTemplate restTemplate, WithdrawRepository withdrawRepository, CustomerRestService customerRestService, BillRestService billRestService, AccountRestService accountRestService) {
        this.restTemplate = restTemplate;
        this.withdrawRepository = withdrawRepository;
        this.customerRestService = customerRestService;
        this.billRestService = billRestService;
        this.accountRestService = accountRestService;
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
        AccountResponseDTO account = accountRestService.getAccountById(accountId);
        if (billId != null) {
            BillResponseDTO billResponseDTO = billRestService.getBillById(billId);
            if (billResponseDTO.getAmount().compareTo(amount) > 0) {
                BillDTO billRequestDTO = BillDTO.builder()
                        .accountId(account.getAccountId())
                        .amount(billResponseDTO.getAmount().subtract(amount))
                        .overdraftEnabled(billResponseDTO.getOverdraftEnabled())
                        .build();
                restTemplate.put(billServiceUrl.concat(billId.toString()), billRequestDTO);
                withdrawRepository.save(
                        new Withdraw(OffsetDateTime.now(), billId, account.getEmail(), amount));
                return new Withdraw(OffsetDateTime.now(), billId, account.getEmail(), amount);
            } else {
                throw new NotEnoughMoneyException("Not enough funds on bill with id " + billId);
            }
        }
        BillResponseDTO defaultBill = billRestService.getDefaultBillByAccountId(accountId);
        if (defaultBill.getAmount().compareTo(amount) > 0) {
            BillDTO billRequestDTO = BillDTO.builder()
                    .accountId(account.getAccountId())
                    .amount(defaultBill.getAmount().subtract(amount))
                    .isDefault(defaultBill.getIsDefault())
                    .overdraftEnabled(defaultBill.getOverdraftEnabled())
                    .build();
            restTemplate.put(billServiceUrl.concat(defaultBill.getBillId().toString()), billRequestDTO);
            withdrawRepository.save(
                    new Withdraw(OffsetDateTime.now(), billId, account.getEmail(), amount));
            return new Withdraw(OffsetDateTime.now(), billId, account.getEmail(), amount);
        } else {
            throw new WithdrawException("Not enough funds on bill with id " + billId);
        }
    }
}
