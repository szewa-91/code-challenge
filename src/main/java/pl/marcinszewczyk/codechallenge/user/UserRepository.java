package pl.marcinszewczyk.codechallenge.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserByName(String name);

    User save(User newUser);
}
