package project.boardservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import project.boardservice.dto.PostDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;
    private String writer;
    private Long view;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    // 조회수 증가
    public void increaseView() {
        this.view += 1;
    }

    public void updatePost(String title, String content, LocalDateTime modifiedDate) {
        this.title = title;
        this.content = content;
        this.modifiedDate = modifiedDate;
    }

    // 생성 메서드
    public static Post createPost(Member member, PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setView(0L);
        post.setCreatedDate(LocalDateTime.now());
        post.setModifiedDate(LocalDateTime.now());
        post.setMember(member);
        post.setWriter(member.getNickname());
        return post;
    }
}


