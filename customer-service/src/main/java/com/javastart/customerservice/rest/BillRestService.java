package com.javastart.customerservice.rest;

import com.javastart.customerservice.controller.dto.BillResponseDTO;
import com.javastart.customerservice.exceptions.DepositException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
public class BillRestService {

    @Value("${internal.bill.service.url}")
    private String billServiceUrl;

    private final RestTemplate restTemplate;

    public BillRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BillResponseDTO> getBillsByAccountId(Long accountId) {
        StringJoiner billUrlBuilder = new StringJoiner("");
        billUrlBuilder.add(billServiceUrl).add("account/").add(String.valueOf(accountId));

        ResponseEntity<BillResponseDTO[]> billResponse = restTemplate.getForEntity(billUrlBuilder.toString(),
                BillResponseDTO[].class);
        return Arrays.asList(Objects.requireNonNull(billResponse.getBody()));
    }

    public BillResponseDTO getBillById(Long billId) {
        StringBuilder sb = new StringBuilder(billServiceUrl);
        sb.append(billId);
        ResponseEntity<BillResponseDTO> billResponse = restTemplate
                .getForEntity(sb.toString(), BillResponseDTO.class);
        return billResponse.getBody();
    }

    public BillResponseDTO getDefaultBillByAccountId(Long accountId) {
        StringBuilder sb = new StringBuilder(billServiceUrl);
        sb.append("account").append("/").append(accountId);
        ResponseEntity<BillResponseDTO[]> billResponse = restTemplate
                .getForEntity(sb.toString(), BillResponseDTO[].class);
        List<BillResponseDTO> billResponseDto = Arrays.asList(Objects.requireNonNull(billResponse.getBody()));
        return billResponseDto.stream()
                .filter(BillResponseDTO::getIsDefault)
                .findAny()
                .orElseThrow(()
                        -> new DepositException("Not fount default bill on account with id " + accountId));
    }
}
