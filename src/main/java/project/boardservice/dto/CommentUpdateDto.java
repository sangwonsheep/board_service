package project.boardservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentUpdateDto {

    @NotEmpty
    private String content;
    private LocalDateTime modifiedDate;

    public CommentUpdateDto(String content) {
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }
}
