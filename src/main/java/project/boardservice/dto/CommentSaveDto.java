package project.boardservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentSaveDto {

    @NotEmpty(message = "댓글은 비어있을 수 없습니다.")
    private String content;

    public CommentSaveDto(String content) {
        this.content = content;
    }
}
