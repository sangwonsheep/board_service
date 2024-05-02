package project.boardservice.repository;

import lombok.Data;

@Data
public class MemberUpdateDto {

    private String name;
    private String password;
    private String nickname;

    public MemberUpdateDto() {
    }

    public MemberUpdateDto(String name, String password, String nickname) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
    }
}
