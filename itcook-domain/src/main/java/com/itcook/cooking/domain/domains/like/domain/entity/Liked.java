package com.itcook.cooking.domain.domains.like.domain.entity;

import com.itcook.cooking.domain.common.events.Events;
import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
import com.itcook.cooking.domain.domains.like.domain.entity.validator.LikedValidator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Liked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_id")
    private Long id;

    @Column(name = "user_id")
    private Long itCookUserId;

    @Column(name = "post_id")
    private Long postId;

    @Builder
    private Liked(Long itCookUserId, Long postId) {
        this.itCookUserId = itCookUserId;
        this.postId = postId;
    }

    public static Liked addLiked(Long userId, Long postId, LikedValidator likedValidate) {
        Liked liked = Liked.builder()
                .itCookUserId(userId)
                .postId(postId)
                .build();
        likedValidate.validateAdd(liked);
        return liked;
    }

    @PostPersist
    public void registerEvent() {
        Events.raise(UserLikedEvent.of(postId, itCookUserId));
    }

}
