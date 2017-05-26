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
import org.springframework.web.bind.annotation.*;

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
        */

        return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    //TODO study stream(), collect()
    //TODO stream() versus parallelStream()
    // /accounts?page=0&size=20&sort=username,asc&joined,desc
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity getAccounts(Pageable pageable) {
        Page<Account> page = repository.findAll(pageable);
        List<AccountDto.Response> contents = page.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDto.Response.class)).collect(Collectors.toList());

        return new ResponseEntity<>(new PageImpl<>(contents, pageable, page.getTotalElements()), HttpStatus.OK);
    }

    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public AccountDto.Response getAccount(@PathVariable Long id) {
        return modelMapper.map(service.getAccount(id), AccountDto.Response.class);
    }

    /*
    전체 업데이트(put) vs 부분 업데이트(patch)
    전체업데이트(put) : password:"pass", fullName:"bh son"
    부분업데이트(patch) :
         - username:"sbh2ch"
         - username:"sbh2ch", password:"pass"
         - password:"pass", fullName:"bh son"
     */
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateAccount(@PathVariable long id, @RequestBody @Valid AccountDto.Update updateDto, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(modelMapper.map(service.updateAccount(id, updateDto), AccountDto.Response.class), HttpStatus.OK);
    }


    @ExceptionHandler(AccountDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserDuplicatedException(AccountDuplicatedException e) {
        return new ErrorResponse("duplicated.username.exception", "[" + e.getUsername() + "] 중복 username");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
        return new ErrorResponse("not.found.account.exception", "[" + e.getId() + "] 에 해당하는 계정이 없습니다.");
    }

    //TODO Hateos
    //TODO 뷰 NSPA 1. Thymeleaf   ||||   SPA 2. React
}