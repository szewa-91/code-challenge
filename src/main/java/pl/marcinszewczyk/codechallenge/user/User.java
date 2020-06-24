package pl.marcinszewczyk.codechallenge.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private final String name;
    private final LocalDateTime since;
    private final Set<User> followed;

    public User(String name) {
        this.name = name;
        this.followed = new HashSet<>();
        this.since = LocalDateTime.now();
    }

    public void follow(User user) {
        this.followed.add(user);
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getSince() {
        return since;
    }

    public Set<User> getFollowed() {
        return followed;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                "name='" + name + '\'' +
                ", since=" + since +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(since, user.since) &&
                Objects.equals(followed, user.followed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, since, followed);
    }
}
