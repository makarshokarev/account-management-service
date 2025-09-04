package com.fintech.exception;

public class DuplicatePhoneNumberException extends RuntimeException {

    public DuplicatePhoneNumberException(String phoneNr) {
        super("Phone number already exists: " + phoneNr);
    }
}
