package project.boardservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentSaveDto {

    @NotEmpty
    private String content;

    public CommentSaveDto(String content) {
        this.content = content;
    }
}
