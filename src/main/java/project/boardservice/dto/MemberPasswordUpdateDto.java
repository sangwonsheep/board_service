package project.boardservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.boardservice.domain.Member;

@Data
public class MemberPasswordUpdateDto {

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
    public MemberPasswordUpdateDto(String password) {
        this.password = password;
    }
}
