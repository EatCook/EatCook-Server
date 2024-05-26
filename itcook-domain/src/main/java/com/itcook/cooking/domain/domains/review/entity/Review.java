package com.itcook.cooking.domain.domains.review.entity;

import com.itcook.cooking.domain.common.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity<Review> {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;
    private String comment; // 리뷰 작성 내용
    private Long postId;
    private Long userId;

    @Builder
    public Review(Long id, String comment, Long postId) {
        this.id = id;
        this.comment = comment;
        this.postId = postId;
    }
}
