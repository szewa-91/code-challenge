package pl.marcinszewczyk.codechallenge.content;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private final String message;
    private final String author;
    private final LocalDateTime created;

    public Post(String message, String author) {
        this.message = message;
        this.author = author;
        this.created = LocalDateTime.now();
    }

    public Post(String message, String author, LocalDateTime created) {
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