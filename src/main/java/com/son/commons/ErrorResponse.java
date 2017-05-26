package com.son.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by kiost on 2017-05-24.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    //private List<FiledError> errors;


    //todo 나중에
    public static class FiledError {
        private String field;
        private String value;
        private String reason;
    }
}
