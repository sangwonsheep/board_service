package project.boardservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {

    private String title;
    private String content;
    private LocalDateTime modifiedDate;

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }
}
