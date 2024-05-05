package project.boardservice.exception;

// 회원 이름 중복 예외
public class MemberNicknameDuplicateException extends RuntimeException{

    public MemberNicknameDuplicateException() {
    }

    public MemberNicknameDuplicateException(String message) {
        super(message);
    }

    public MemberNicknameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNicknameDuplicateException(Throwable cause) {
        super(cause);
    }
}
