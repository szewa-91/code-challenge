package pl.marcinszewczyk.codechallenge.user;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void follow(String followingUserName, String followedUserName) {
        User followedUser = findUserOrThrowException(followedUserName);
        User followingUser = createUserIfDoesNotExist(followingUserName);

        followingUser.follow(followedUser);
    }

    public User createUserIfDoesNotExist(String name) {
        return findUser(name).orElseGet(() -> {
            User newUser = new User(name);
            userRepository.save(newUser);
            return newUser;
        });
    }

    public Optional<User> findUser(String name) {
        return userRepository.getUserByName(name);
    }

    private User findUserOrThrowException(String followingUserName) {
        return userRepository.getUserByName(followingUserName)
                .orElseThrow(() -> new UserNotFoundException(followingUserName));
    }
}
