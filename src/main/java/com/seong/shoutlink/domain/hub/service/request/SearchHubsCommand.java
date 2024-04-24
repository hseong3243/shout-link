package com.seong.shoutlink.domain.hub.service.request;

public record SearchHubsCommand(String tagKeyword, int page, int size) {

}
