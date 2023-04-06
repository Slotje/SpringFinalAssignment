package nl.slotboom.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User with same username already exists");
    }
}
