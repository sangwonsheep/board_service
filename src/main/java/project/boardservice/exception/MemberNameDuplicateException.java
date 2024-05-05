package project.boardservice.exception;

// 회원 아이디 중복 예외
public class MemberNameDuplicateException extends RuntimeException{

    public MemberNameDuplicateException() {
    }

    public MemberNameDuplicateException(String message) {
        super(message);
    }

    public MemberNameDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNameDuplicateException(Throwable cause) {
        super(cause);
    }
}
