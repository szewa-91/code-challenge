package pl.marcinszewczyk.codechallenge.infrastructure;

import org.springframework.stereotype.Component;
import pl.marcinszewczyk.codechallenge.user.User;
import pl.marcinszewczyk.codechallenge.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class InMemoryUserRepository implements UserRepository {
    final Collection<User> storage = new ArrayList<>();

    @Override
    public Optional<User> getUserByName(String name) {
        return storage.stream().filter(post -> post.getName().equals(name)).findFirst();
    }

    @Override
    public User save(User post) {
        storage.add(post);
        return post;
    }

    public void clear() {
        storage.clear();
    }
}
