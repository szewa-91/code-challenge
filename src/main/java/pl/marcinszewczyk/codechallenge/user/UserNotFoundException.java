package pl.marcinszewczyk.codechallenge.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userName) {
        super(String.format("User %s doesn't exist", userName));
    }
}
