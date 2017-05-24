package com.son.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by kiost on 2017-05-24.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
}
