package pl.marcinszewczyk.codechallenge.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    public static final String TEST_USER_NAME = "user";
    public static final String FOLLOWED_USER_NAME = "followedUser1";

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;


    @Test
    public void shouldFollowUser() {
        final User testUser = user(TEST_USER_NAME);
        final User followedUser = user(FOLLOWED_USER_NAME);
        when(userService.findUser(TEST_USER_NAME)).thenReturn(Optional.of(testUser));
        when(userService.findUser(FOLLOWED_USER_NAME)).thenReturn(Optional.of(followedUser));

        userService.follow(TEST_USER_NAME, FOLLOWED_USER_NAME);

        assertThat(testUser.getFollowed()).containsExactly(followedUser);
    }

    @Test
    public void shouldThrowExceptionWhenTriesToFollowNonexistentUser() {
        when(userService.findUser(FOLLOWED_USER_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.follow(TEST_USER_NAME, FOLLOWED_USER_NAME))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User " + FOLLOWED_USER_NAME + " doesn't exist");
    }

    private static User user(String userName, User... followedUsers) {
        User user = new User(userName);
        Stream.of(followedUsers).forEach(user::follow);
        return user;
    }
}