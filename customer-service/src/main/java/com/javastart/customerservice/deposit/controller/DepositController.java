package com.javastart.customerservice.deposit.controller;

import com.javastart.customerservice.deposit.controller.dto.DepositRequestDTO;
import com.javastart.customerservice.deposit.controller.dto.DepositResponseDTO;
import com.javastart.customerservice.deposit.service.DepositService;
import com.javastart.customerservice.rest.BillRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DepositController {

    private final DepositService depositService;

    private final BillRestService billRestService;

    @Autowired
    public DepositController(DepositService depositService, BillRestService billRestService) {
        this.depositService = depositService;
        this.billRestService = billRestService;
    }

    @GetMapping("/deposit/{id}")
    public DepositResponseDTO getById(@PathVariable("id") Long id) {
        return new DepositResponseDTO(depositService.getById(id));
    }

    @GetMapping("/deposit/account/{accountEmail}")
    public List<DepositResponseDTO> getAllByAccountEmail(@PathVariable("accountEmail") String email) {
        return depositService.getAllByAccountEmail(email)
                .stream()
                .map(DepositResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/deposit/bill/{billId}")
    public List<DepositResponseDTO> getAllByBillId(@PathVariable("billId") Long billId) {
        return depositService.getAllByBillId(billId)
                .stream().map(DepositResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/deposit")
    public DepositResponseDTO deposit(@RequestBody DepositRequestDTO requestDTO) {
        if(billRestService.getDefaultBillByAccountId(requestDTO.getAccountId()) != null) {
            return new DepositResponseDTO(depositService.deposit(requestDTO.getAccountId(), requestDTO.getBillId(),
                    requestDTO.getAmount()),
                    billRestService.getDefaultBillByAccountId(requestDTO.getAccountId()).getAmount());
        }
        return new DepositResponseDTO(depositService.deposit(requestDTO.getAccountId(), requestDTO.getBillId(),
                requestDTO.getAmount()), billRestService.getBillById(requestDTO.getBillId()).getAmount());
    }
}
