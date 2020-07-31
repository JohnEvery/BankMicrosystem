package com.javastart.accountservice.service;

import com.javastart.accountservice.entity.Account;
import com.javastart.accountservice.exceptions.AccountNotFoundException;
import com.javastart.accountservice.exceptions.EmailAlreadyExistException;
import com.javastart.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(()
                -> new AccountNotFoundException("Unable to find account with id: " + id));
    }

    public Account getAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(()
                -> new AccountNotFoundException("Unable to find account with email " + email));
    }

    public Long saveAccount(String name, String email, String phone, List<Long> bills) throws
            EmailAlreadyExistException {
        if (!isEmailAlreadyExist(email)) {
            throw new EmailAlreadyExistException("This email is already used.");
        }
        Account account = new Account(name, email, phone,
                bills, OffsetDateTime.now());
        return accountRepository.save(account).getId();
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account updateAccount(Long accountId, String name, String email, String phone, List<Long> bills) {
        OffsetDateTime creationDate = getAccountById(accountId).getCreationDate();
        Account account = new Account(name, email, phone, bills, creationDate);
        account.setId(accountId);
        accountRepository.save(account);
        return account;
    }

    public Account deleteAccount(Long accountId) {
        Account account = getAccountById(accountId);
        accountRepository.deleteById(accountId);
        return account;
    }

    public Boolean isEmailAlreadyExist(String email) {
        return !accountRepository.findAccountByEmail(email).isPresent();
    }

}
