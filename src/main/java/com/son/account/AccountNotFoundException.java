package com.son.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by kiost on 2017-05-26.
 */
@Getter
@AllArgsConstructor
public class AccountNotFoundException extends RuntimeException {
    Long id;
}
