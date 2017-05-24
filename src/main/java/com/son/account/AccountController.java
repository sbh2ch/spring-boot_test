package com.son.account;

import com.son.commons.ErrorResponse;
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
        if (result.hasErrors()) {
            return new ResponseEntity<>(new ErrorResponse("bad.request", "잘못된 요청"), HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);
        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }
}
