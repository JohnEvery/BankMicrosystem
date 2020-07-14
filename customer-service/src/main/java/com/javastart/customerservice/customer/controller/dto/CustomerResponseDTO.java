package com.javastart.customerservice.customer.controller.dto;

import com.javastart.customerservice.controller.dto.AccountResponseDTO;
import com.javastart.customerservice.controller.dto.BillResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomerResponseDTO {

    private AccountResponseDTO accountResponseDTO;

    private List<BillResponseDTO> billResponseDTO;
}
