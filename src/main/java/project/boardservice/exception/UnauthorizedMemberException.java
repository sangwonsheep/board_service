package project.boardservice.exception;

// 허가되지 않은 사용자 예외
public class UnauthorizedMemberException extends RuntimeException {
    public UnauthorizedMemberException() {
    }

    public UnauthorizedMemberException(String message) {
        super(message);
    }

    public UnauthorizedMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedMemberException(Throwable cause) {
        super(cause);
    }
}
