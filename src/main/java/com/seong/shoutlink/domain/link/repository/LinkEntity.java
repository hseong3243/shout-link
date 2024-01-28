package com.seong.shoutlink.domain.link.repository;

import com.seong.shoutlink.domain.link.Link;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    private String url;
    private String description;

    private LinkEntity(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public static LinkEntity create(Link link) {
        return new LinkEntity(link.getUrl(), link.getDescription());
    }
}
