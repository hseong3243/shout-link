package com.seong.shoutlink.global.auth;

import com.seong.shoutlink.domain.auth.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthTestController {

    @GetMapping("/login-user")
    public ResponseEntity<Long> loginUser(@LoginUser Long memberId) {
        return ResponseEntity.ok(memberId);
    }
}
