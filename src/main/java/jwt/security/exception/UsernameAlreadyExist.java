package jwt.security.exception;

public class UsernameAlreadyExist extends RuntimeException {

	public UsernameAlreadyExist() {
	}

	public UsernameAlreadyExist(String message) {
		super(message);
	}

	public UsernameAlreadyExist(String message, Throwable cause) {
		super(message, cause);
	}
}
