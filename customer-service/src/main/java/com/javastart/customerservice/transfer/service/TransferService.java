package com.javastart.customerservice.transfer.service;

import com.javastart.customerservice.controller.dto.AccountResponseDTO;
import com.javastart.customerservice.controller.dto.BillDTO;
import com.javastart.customerservice.controller.dto.BillResponseDTO;
import com.javastart.customerservice.exceptions.TransferExceptions;
import com.javastart.customerservice.exceptions.NotEnoughMoneyException;
import com.javastart.customerservice.rest.AccountRestService;
import com.javastart.customerservice.rest.BillRestService;
import com.javastart.customerservice.rest.CustomerRestService;
import com.javastart.customerservice.transfer.entity.Transfer;
import com.javastart.customerservice.transfer.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    private final AccountRestService accountRestService;

    private final BillRestService billRestService;

    private final CustomerRestService customerRestService;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRestService accountRestService, BillRestService billRestService, CustomerRestService customerRestService) {
        this.transferRepository = transferRepository;
        this.accountRestService = accountRestService;
        this.billRestService = billRestService;
        this.customerRestService = customerRestService;
    }

    public Transfer getById(Long transferId) {
        return transferRepository.getById(transferId).orElseThrow(()
                -> new TransferExceptions("Unable to find transfer with id " + transferId));
    }

    public List<Transfer> getAllByEmail(String accountEmail) {
        return transferRepository.getAllByEmail(accountEmail).orElseThrow(()
                -> new TransferExceptions("Unable to find outgoing transfers at account with email " + accountEmail));
    }

    public List<Transfer> getAllByBillId(Long billId) {
        return transferRepository.getAllByBillId(billId).orElseThrow(()
                -> new TransferExceptions("Unable to find transfers at bill with id " + billId));
    }

    @Transactional
    public Transfer transfer(Long billIdFrom, Long billIdTo,
                             Long accountIdFrom, Long accountIdTo, BigDecimal amount) {
        customerRestService.checkAmount(amount);
        if(billIdFrom != null && billIdTo != null) {
            BillResponseDTO billResponseDTOFrom = billRestService.getBillById(billIdFrom);
            BillResponseDTO billResponseDTOTo = billRestService.getBillById(billIdTo);
            if(billResponseDTOFrom.getAmount().compareTo(amount) > 0) {
                BillDTO billRequestFrom = BillDTO.builder()
                        .accountId(billResponseDTOFrom.getAccountId())
                        .amount(billResponseDTOFrom.getAmount().subtract(amount))
                        .isDefault(billResponseDTOFrom.getIsDefault())
                        .overdraftEnabled(billResponseDTOFrom.getOverdraftEnabled())
                        .build();
                BillDTO billRequestTo = BillDTO.builder()
                        .accountId(billResponseDTOTo.getAccountId())
                        .amount(billResponseDTOTo.getAmount().add(amount))
                        .isDefault(billResponseDTOTo.getIsDefault())
                        .overdraftEnabled(billResponseDTOTo.getOverdraftEnabled())
                        .build();
                billRestService.updateBills(billIdFrom, billIdTo, Arrays.asList(billRequestFrom, billRequestTo));
                String emailFrom = accountRestService.getAccountById(billResponseDTOFrom.getAccountId()).getEmail();
                String emailTo = accountRestService.getAccountById(billResponseDTOTo.getAccountId()).getEmail();
                transferRepository.save(new Transfer(OffsetDateTime.now(),
                        billIdFrom, billIdTo, emailFrom, emailTo, amount));
                return  new Transfer(OffsetDateTime.now(), billIdFrom, billIdTo, emailFrom, emailTo, amount);
            } else {
                throw new NotEnoughMoneyException("Not enough funds on bill with id " + billIdFrom);
            }
        }
        AccountResponseDTO accountFrom = accountRestService.getAccountById(accountIdFrom);
        AccountResponseDTO accountTo = accountRestService.getAccountById(accountIdTo);
        BillResponseDTO defaultBillResponseFrom = billRestService.getDefaultBillByAccountId(accountIdFrom);
        BillResponseDTO defaultBillResponseTo = billRestService.getDefaultBillByAccountId(accountIdTo);
        if(defaultBillResponseFrom.getAmount().compareTo(amount) > 0) {
            BillDTO billRequestDTOFrom = BillDTO.builder().accountId(accountIdFrom)
                    .isDefault(defaultBillResponseFrom.getIsDefault())
                    .amount(defaultBillResponseFrom.getAmount().subtract(amount))
                    .overdraftEnabled(defaultBillResponseFrom.getOverdraftEnabled())
                    .build();
            BillDTO billRequestDTOTo = BillDTO.builder().accountId(accountIdTo)
                    .isDefault(defaultBillResponseTo.getIsDefault())
                    .amount(defaultBillResponseTo.getAmount().add(amount))
                    .overdraftEnabled(defaultBillResponseTo.getOverdraftEnabled())
                    .build();
            billRestService.updateBills(defaultBillResponseFrom.getBillId(),
                    defaultBillResponseTo.getBillId(), Arrays.asList(billRequestDTOFrom, billRequestDTOTo));
            transferRepository.save(new Transfer(OffsetDateTime.now(), defaultBillResponseFrom.getBillId(),
                    defaultBillResponseTo.getBillId(), accountFrom.getEmail(), accountTo.getEmail(), amount));
            return new Transfer(OffsetDateTime.now(), defaultBillResponseFrom.getBillId(),
                    defaultBillResponseTo.getBillId(), accountFrom.getEmail(), accountTo.getEmail(), amount);
        } else {
            throw new NotEnoughMoneyException("Not enough funds on bill with id "
                    + defaultBillResponseFrom.getBillId());
        }
    }
}
