package com.example.exceptionhandle;

public class UserNotEnable extends Exception {

    public UserNotEnable(String msg) {
        super(msg);
    }

    public UserNotEnable(String msg, Throwable cause) {
        super(msg, cause);
    }

}
