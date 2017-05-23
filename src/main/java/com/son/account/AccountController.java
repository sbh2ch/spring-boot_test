package com.son.account;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by kiost on 2017-05-23.
 */
@Controller
public class AccountController {
    @Autowired
    private AccountService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result) {
        System.out.println("**********"+create.getUsername());
        System.out.println(create.getPassword());
        if (result.hasErrors()) {
            //TODO 에러 응답 본문 추가하기
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);
        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }
}
