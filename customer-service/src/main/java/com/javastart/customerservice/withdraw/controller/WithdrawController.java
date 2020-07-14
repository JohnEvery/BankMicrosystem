package com.javastart.customerservice.withdraw.controller;

import com.javastart.customerservice.withdraw.controller.dto.WithdrawRequestDTO;
import com.javastart.customerservice.withdraw.controller.dto.WithdrawResponseDTO;
import com.javastart.customerservice.withdraw.entity.Withdraw;
import com.javastart.customerservice.withdraw.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WithdrawController {

    private final WithdrawService withdrawService;

    @Autowired
    public WithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @GetMapping("/withdraw/{id}")
    public WithdrawResponseDTO getById(@PathVariable Long id) {
        return new WithdrawResponseDTO(withdrawService.getWithdrawById(id));
    }

    @GetMapping("/withdraw/account/{accountEmail}")
    public List<WithdrawResponseDTO> getAllByAccountId(@PathVariable String accountEmail) {
        return withdrawService.getAllByAccountEmail(accountEmail)
                .stream()
                .map(WithdrawResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/withdraw/bill/{id}")
    public List<WithdrawResponseDTO> getAllByBillId(@PathVariable Long billId) {
        return withdrawService.getAllByBillId(billId)
                .stream()
                .map(WithdrawResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/withdraw")
    public WithdrawResponseDTO withdraw(@RequestBody WithdrawRequestDTO requestDTO) {
        return new WithdrawResponseDTO(withdrawService.withdraw(requestDTO.getBillId(),
                requestDTO.getAccountId(), requestDTO.getAmount()));
    }
}
