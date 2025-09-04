package com.fintech.util;

import java.util.regex.Pattern;

public class PhoneNumberValidator {

    private static final String E164_PATTERN = "^\\+[1-9]\\d{6,14}$";
    private static final Pattern COMPILED_PATTERN = Pattern.compile(E164_PATTERN);

    public static boolean isValidE164(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        return COMPILED_PATTERN.matcher(phoneNumber.trim()).matches();
    }
}
