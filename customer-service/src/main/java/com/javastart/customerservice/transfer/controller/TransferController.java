package com.javastart.customerservice.transfer.controller;

import com.javastart.customerservice.transfer.controller.dto.TransferRequestDTO;
import com.javastart.customerservice.transfer.controller.dto.TransferResponseDTO;
import com.javastart.customerservice.transfer.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/transfer/{id}")
    public TransferResponseDTO getById(@PathVariable Long transferId) {
        return new TransferResponseDTO(transferService.getById(transferId));
    }

    @GetMapping("/transfer/account/{accountEmail}")
    public List<TransferResponseDTO> getAllOutgoingByAccountEmail(@PathVariable String accountEmail) {
        return transferService.getAllByEmail(accountEmail)
                .stream()
                .map(TransferResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/transfer/bill/{billId}")
    public List<TransferResponseDTO> getAllByBillId(@PathVariable Long billId) {
        return transferService.getAllByBillId(billId)
                .stream()
                .map(TransferResponseDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/transfer")
    public TransferResponseDTO transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        return new TransferResponseDTO(transferService.transfer(transferRequestDTO.getBillIdFrom(), transferRequestDTO.getBillIdTo(),
                transferRequestDTO.getAccountIdFrom(), transferRequestDTO.getAccountIdTo(),
                transferRequestDTO.getAmount()));
    }
}
