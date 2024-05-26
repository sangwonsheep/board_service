package project.boardservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.boardservice.domain.Member;

@Data
public class MemberUpdateDto {

    private Long id;

    @NotBlank(message = "아이디는 필수 값입니다.")
    @Size(min = 2, max = 10, message = "아이디의 길이는 2~10 사이여야 합니다.")
    private String name;

    @NotBlank(message = "이름은 필수 값입니다.")
    @Size(min = 2, max = 10, message = "이름의 길이는 2~10 사이여야 합니다.")
    private String nickname;

    public MemberUpdateDto() {
    }

    public MemberUpdateDto(String nickname) {
        this.nickname = nickname;
    }
}
