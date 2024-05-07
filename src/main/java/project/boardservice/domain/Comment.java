package project.boardservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.boardservice.dto.CommentSaveDto;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getComments().add(this);
    }

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    // 수정 메서드
    public void updateComment(String content) {
        this.content = content;
    }

    // 생성 메서드
    public static Comment createComment(Member member, Post post, CommentSaveDto commentSaveDto) {
        Comment comment = new Comment();
        comment.setContent(commentSaveDto.getContent());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setModifiedDate(LocalDateTime.now());
        comment.setMember(member);
        comment.setPost(post);
        return comment;
    }

}
