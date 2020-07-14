package com.javastart.customerservice.customer.controller.dto;

import com.javastart.customerservice.controller.dto.AccountRequestDTO;
import com.javastart.customerservice.controller.dto.BillRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CustomerRequestDTO {

    private AccountRequestDTO accountRequestDTO;

    private List<BillRequestDTO> billRequestDTO;

}
