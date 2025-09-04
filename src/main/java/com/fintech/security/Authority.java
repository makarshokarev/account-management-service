package com.fintech.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authority {

    public static final String USER_READ = "hasAuthority('USER_READ')";
    public static final String USER_WRITE= "hasAuthority('USER_WRITE')";
}
