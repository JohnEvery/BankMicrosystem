package com.javastart.accountservice.controller;

import com.javastart.accountservice.controller.dto.AccountRequestDto;
import com.javastart.accountservice.controller.dto.AccountResponseDto;
import com.javastart.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public AccountResponseDto getAccount(@PathVariable("id") Long accountId) {
        return new AccountResponseDto(accountService.getAccountById(accountId));
    }

    @PostMapping("/")
    public Long createAccount(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.saveAccount(accountRequestDto.getName(), accountRequestDto.getEmail(),
                accountRequestDto.getPhone(), accountRequestDto.getBills());
    }

    @GetMapping("/email/{email}")
    public AccountResponseDto getAccountByEmail(@PathVariable String email) {
        return new AccountResponseDto(accountService.getAccountByEmail(email));
    }

    @GetMapping("/")
    public List<AccountResponseDto> getAllAccounts() {
        return accountService.findAll().stream()
                .map(AccountResponseDto::new)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public AccountResponseDto updateAccount(@PathVariable("id") Long accountId,
                                            @Valid @RequestBody AccountRequestDto accountRequestDto) {
        return new AccountResponseDto(accountService.updateAccount(accountId, accountRequestDto.getName(), accountRequestDto.getEmail(),
                accountRequestDto.getPhone(), accountRequestDto.getBills()));
    }

    @DeleteMapping("/{id}")
    public AccountResponseDto deleteAccount(@PathVariable("id") Long accountId) {
        return new AccountResponseDto(accountService.deleteAccount(accountId));
    }
}
