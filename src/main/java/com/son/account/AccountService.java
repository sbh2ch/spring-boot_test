package com.son.account;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by kiost on 2017-05-23.
 */
@Service
@Transactional
@Slf4j
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
        Account account = modelMapper.map(dto, Account.class);

        String username = dto.getUsername();
        if (repository.findByUsername(username) != null) {
            log.error("user duplicated exception. -->> {}", username);
            throw new AccountDuplicatedException(username);
        }

        account.setPassword(passwordEncoder.encode(account.getPassword())); //password 암호화
        Date date = new Date();
        account.setJoined(date);
        account.setUpdated(date);

        return repository.save(account);
    }

    public Account updateAccount(Long id, AccountDto.Update updateDto) {
        Account account = getAccount(id);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setFullName(updateDto.getFullName());
        return repository.save(account);
    }

    public Account getAccount(Long id) {
        Account account = repository.findOne(id);
        if (account == null)
            throw new AccountNotFoundException(id);

        return account;
    }

    public void deleteAccount(Long id) {
        repository.delete(getAccount(id));
    }
}
