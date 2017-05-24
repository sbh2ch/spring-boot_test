package com.son.account;

import org.junit.Test;

/**
 * Created by kiost on 2017-05-23.
 */
public class AccountTest {
    @Test
    public void getterSetter() {
        Account account = new Account();

        account.setUsername("hello");
        account.setPassword("world");
    }
}

