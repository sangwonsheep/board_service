package project.boardservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostUpdateDto {

    @NotEmpty(message = "게시글 제목은 비어 있을 수 없습니다.")
    private String title;
    @NotEmpty(message = "게시글 내용은 비어 있을 수 없습니다.")
    private String content;
    private LocalDateTime modifiedDate;

    public PostUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }
}
