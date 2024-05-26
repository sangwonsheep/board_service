package project.boardservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.boardservice.domain.Member;

@Data
public class MemberPasswordUpdateDto {

    private Long id;

    @NotBlank(message = "아이디는 필수 값입니다.")
    @Size(min = 2, max = 10, message = "아이디의 길이는 2~10 사이여야 합니다.")
    private String name;

    @NotBlank(message = "이름은 필수 값입니다.")
    @Size(min = 2, max = 10, message = "이름의 길이는 2~10 사이여야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String new_password;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이여야 합니다.")
    private String new_password_check;

    public MemberPasswordUpdateDto() {
    }

    // 생성 메서드
    public MemberPasswordUpdateDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.password = member.getPassword();
    }
}
