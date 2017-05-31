package com.son.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by kiost on 2017-05-23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
public class AccountControllerTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private AccountService service;

    MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(filterChainProxy)
                .build();
    }

    private AccountDto.Create accountCreateFixture() {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("sbh2ch");
        createDto.setPassword("password");

        return createDto;
    }

    //@Rollback(false), @Commit // 만약 롤백 시키기 싫다면
    @Test
    public void createAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();

        ResultActions result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));
        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.username", is("sbh2ch")));
    }

    @Test
    public void createAccount_BadRequest() throws Exception {
        AccountDto.Create createDto = new AccountDto.Create();
        createDto.setUsername("2ch");
        createDto.setPassword("a");

        ResultActions result = mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createDto)));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errorCode", is("bad.request")));
    }

    @Test
    public void getAccounts() throws Exception {
        AccountDto.Create createAccount = accountCreateFixture();
        service.createAccount(createAccount);

        ResultActions result = mockMvc.perform(get("/accounts").with(httpBasic(createAccount.getUsername(), createAccount.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }
    // {"content":
    // [{"id":1,"username":"sonbh","fullName":null,"joined":1495692458475,"updated":1495692458475}],
    // "totalElements":1,
    // "totalPages":1,
    // "last":true,
    // "number":0,
    // "size":20,
    // "sort":null,
    // "first":true,
    // "numberOfElements":1}

    @Test
    public void getAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();
        Account account = service.createAccount(createDto);

        ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()).with(httpBasic(createDto.getUsername(), createDto.getPassword())));

        result.andDo(print());
        result.andExpect(status().isOk());
    }

    @Test
    public void updateAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();
        Account account = service.createAccount(createDto);
        AccountDto.Update updateDto = new AccountDto.Update();
        updateDto.setFullName("byunghwa Son");
        updateDto.setPassword("byunghwa");

        ResultActions result = mockMvc.perform(put("/accounts/" + account.getId()).with(httpBasic(createDto.getUsername(), createDto.getPassword())).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDto)));
        result.andDo(print());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.fullName", is("byunghwa Son")));
    }

    @Test
    public void deleteAccount_Error() throws Exception {
        AccountDto.Create createAccount = accountCreateFixture();
        service.createAccount(createAccount);

        ResultActions result = mockMvc.perform(delete("/accounts/2131231230")
                .with(httpBasic(createAccount.getUsername(), createAccount.getPassword())));

        result.andDo(print());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteAccount() throws Exception {
        AccountDto.Create createDto = accountCreateFixture();
        Account account = service.createAccount(createDto);

        ResultActions result = mockMvc.perform(delete("/accounts/" + account.getId())
                .with(httpBasic(createDto.getUsername(), "password")));

        result.andDo(print());
        result.andExpect(status().isNoContent());
    }
}