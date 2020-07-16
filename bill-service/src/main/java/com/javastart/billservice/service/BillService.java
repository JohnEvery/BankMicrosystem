package com.javastart.billservice.service;

import com.javastart.billservice.controller.dto.BillRequestDto;
import com.javastart.billservice.entity.Bill;
import com.javastart.billservice.exceptions.BillNotFoundException;
import com.javastart.billservice.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {

    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElseThrow(()
                -> new BillNotFoundException("Unable to find bill with id: " + id));
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.findBillsByAccountId(accountId).orElseThrow(()
                -> new BillNotFoundException("Unable to find bill with account id " + accountId));
    }

    public Long saveBill(Long accountId, Boolean isDefault, BigDecimal amount,
                         Boolean overdraftEnabled) {
        return billRepository.save(new Bill(accountId, isDefault,
                amount, overdraftEnabled, OffsetDateTime.now())).getId();
    }

    public List<Long> saveAll(List<BillRequestDto> billRequestDTO) {
        return billRepository.saveAll(billRequestDTO.stream().map(dto
                -> new Bill(dto.getAccountId(), dto.getIsDefault(), dto.getAmount(),
                dto.getOverdraftEnabled(), OffsetDateTime.now()))
                .collect(Collectors.toList()))
                .stream()
                .map(Bill::getId)
                .collect(Collectors.toList());
    }

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public Bill updateBill(Long billId, Long accountId, Boolean isDefault, BigDecimal amount,
                           Boolean overdraftEnabled) {
        OffsetDateTime billCreatedDate = getBillById(billId).getCreateDate();
        Bill bill = new Bill(accountId, isDefault, amount, overdraftEnabled, billCreatedDate);
        bill.setId(billId);
        return billRepository.save(bill);
    }

    @Transactional
    public List<Bill> updateBills(Long billIdFrom, Long billIdTo, List<BillRequestDto> billRequestDtos) {
        List<Bill> bills = billRequestDtos.stream().map(dto
                -> new Bill(dto.getAccountId(), dto.getIsDefault(), dto.getAmount(),
                dto.getOverdraftEnabled(), OffsetDateTime.now()))
                .collect(Collectors.toList());
        Bill billFrom = bills.get(0);
        Bill billTo = bills.get(1);
        billFrom.setId(billIdFrom);
        billTo.setId(billIdTo);
        billRepository.save(billFrom);
        billRepository.save(billTo);
        return Arrays.asList(billFrom, billTo);
    }

    public Bill deleteBill(Long id) {
        Bill deletedBill = getBillById(id);
        billRepository.deleteById(id);
        return deletedBill;
    }
}