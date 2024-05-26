package com.seong.shoutlink.domain.tag.service;

public class NotMetCondition extends RuntimeException{

    public NotMetCondition(String message) {
        super(message);
    }
}
