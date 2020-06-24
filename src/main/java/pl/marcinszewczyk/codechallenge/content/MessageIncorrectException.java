package pl.marcinszewczyk.codechallenge.content;

public class MessageIncorrectException extends RuntimeException {
    public MessageIncorrectException(String message) {
        super(message);
    }
}
