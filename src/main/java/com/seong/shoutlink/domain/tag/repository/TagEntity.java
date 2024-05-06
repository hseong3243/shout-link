package com.seong.shoutlink.domain.tag.repository;

import com.seong.shoutlink.domain.common.BaseEntity;
import com.seong.shoutlink.domain.hub.repository.HubEntity;
import com.seong.shoutlink.domain.member.repository.MemberEntity;
import com.seong.shoutlink.domain.tag.Tag;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tag")
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String name;

    protected TagEntity(String name) {
        this.name = name;
    }

    public static TagEntity create(Tag tag, HubEntity hubEntity) {
        return new HubTagEntity(tag.getName(), hubEntity);
    }

    public static TagEntity create(Tag tag, MemberEntity memberEntity) {
        return new MemberTagEntity(tag.getName(), memberEntity);
    }

    public Tag toDomain() {
        return new Tag(tagId, name, getCreatedAt(), getUpdatedAt());
    }
}
