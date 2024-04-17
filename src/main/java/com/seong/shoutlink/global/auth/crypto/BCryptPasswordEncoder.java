package com.seong.shoutlink.global.auth.crypto;

import com.seong.shoutlink.domain.member.service.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean isMatches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    @Override
    public boolean isNotMatches(String rawPassword, String encodedPassword) {
        return !isMatches(rawPassword, encodedPassword);
    }
}
