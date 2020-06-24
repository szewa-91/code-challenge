package pl.marcinszewczyk.codechallenge.content;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    public static final int MAXIMUM_LENGTH = 140;
    private String message;
    private String author;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;

    private Post() {
        // only for deserialization
    }

    public Post(String message, String author) {
        this(message, author, LocalDateTime.now());
    }

    public Post(String message, String author, LocalDateTime created) {
        validate(message);
        this.message = message;
        this.author = author;
        this.created = created;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Post{" +
                "message='" + message + '\'' +
                ", author='" + author + '\'' +
                ", created=" + created +
                '}';
    }

    private void validate(String message) {
        if (message == null) {
            throw new MessageIncorrectException("The message is null.");
        }
        if (message.length() == 0) {
            throw new MessageIncorrectException("The message is empty.");
        }
        if (message.length() > MAXIMUM_LENGTH) {
            throw new MessageIncorrectException(String.format("Max length is %s characters.", MAXIMUM_LENGTH));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(message, post.message) &&
                Objects.equals(author, post.author) &&
                Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, author, created);
    }
}
