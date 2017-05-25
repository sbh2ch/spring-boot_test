package com.son.account;

import com.son.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kiost on 2017-05-23.
 */
@Controller
public class AccountController {
    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result) {
        if (result.hasErrors()) {
            //Todo bindingResult 안에 들어있는 에러 정보 활용이 필요
            return new ResponseEntity<>(new ErrorResponse("bad.request", "잘못된 요청"), HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);

        /*
        1. 리턴타입으로 판단 .. 애매, 지저분함
        2. 파라미터 이용. 1번에 비해선 좀 더 직관적임 + 지저분한건 마찬가지
        3. 서비스에서 바로 예외 던지기
        4. 콜백을 이용한 비동기식 예외처리
        * */

        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity handleUserDuplicatedException(UserDuplicatedException e) {
        return new ResponseEntity<>(new ErrorResponse("duplicated.username.exception", "[" + e.getUsername() + "] 중복 username"), HttpStatus.BAD_REQUEST);
    }

    // stream() versus parallelStream()
    // /accounts?page=0&size=20&sort=username,asc&joined,desc
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity getAccounts(Pageable pageable) {
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> contents = page.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDto.Response.class)).collect(Collectors.toList());

        PageImpl<AccountDto.Response> result = new PageImpl<>(contents, pageable, page.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}