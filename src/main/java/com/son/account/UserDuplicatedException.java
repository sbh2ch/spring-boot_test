package com.son.account;

import lombok.Getter;

/**
 * Created by kiost on 2017-05-24.
 */
@Getter
public class UserDuplicatedException extends RuntimeException {
    private String username;

    public UserDuplicatedException(String username) {
        this.username = username;
    }
}
