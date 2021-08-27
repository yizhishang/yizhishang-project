package com.yizhishang.oauth.handler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @since 2020/1/8 14:18
 * @author yizhishang
 */
public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    public CustomPasswordEncoder(int strength) {
        super(strength);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return super.matches(rawPassword, encodedPassword);
    }

}
