package com.elfec.ssc.model.exceptions;

/**
 * Created by drodriguez on 06/07/2016.
 * Api related exception
 */
public class ApiException extends Exception {
    private String message;
    private String key;

    public ApiException() {
    }

    public ApiException(String message, String key) {
        super(message);
        this.key = key;
    }

    @Override
    public String getMessage(){
        return message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}