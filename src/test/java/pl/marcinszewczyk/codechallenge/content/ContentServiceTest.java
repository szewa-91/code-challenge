package pl.marcinszewczyk.codechallenge.content;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.marcinszewczyk.codechallenge.user.User;
import pl.marcinszewczyk.codechallenge.user.UserNotFoundException;
import pl.marcinszewczyk.codechallenge.user.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.LocalDateTime.of;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {
    public static final String TEST_USER_NAME = "user";
    public static final String FOLLOWED_USER_1 = "followedUser1";
    public static final String FOLLOWED_USER_2 = "followedUser2";
    public static final User TEST_USER = user(TEST_USER_NAME,
            user(FOLLOWED_USER_1),
            user(FOLLOWED_USER_2));
    private static final LocalDate TODAY = LocalDate.of(2020, 06, 23);

    @Mock
    private UserService userService;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private ContentService contentService;

    @Test
    public void shouldDisplayTimeline() {
        when(userService.findUser(TEST_USER_NAME)).thenReturn(Optional.of(TEST_USER));
        when(postRepository.getByAuthor(FOLLOWED_USER_1)).thenReturn(asList(
                new Post("Message1", FOLLOWED_USER_1, of(TODAY, LocalTime.of(10, 20))),
                new Post("Message2", FOLLOWED_USER_1, of(TODAY, LocalTime.of(8, 15)))
        ));
        when(postRepository.getByAuthor(FOLLOWED_USER_2)).thenReturn(asList(
                new Post("Message3", FOLLOWED_USER_2, of(TODAY, LocalTime.of(9, 30)))
        ));

        List<Post> timeline = contentService.getTimeline(TEST_USER_NAME);

        assertThat(timeline).extracting(Post::getAuthor, Post::getMessage)
                .containsExactly(
                        tuple(FOLLOWED_USER_1, "Message1"),
                        tuple(FOLLOWED_USER_2, "Message3"),
                        tuple(FOLLOWED_USER_1, "Message2")
                );
    }

    @Test
    public void shouldDisplayWall() {
        when(userService.findUser(TEST_USER_NAME)).thenReturn(Optional.of(TEST_USER));
        when(postRepository.getByAuthor(TEST_USER_NAME)).thenReturn(asList(
                new Post("Message1", TEST_USER_NAME, of(TODAY, LocalTime.of(10, 20))),
                new Post("Message2", TEST_USER_NAME, of(TODAY, LocalTime.of(8, 15))),
                new Post("Message3", TEST_USER_NAME, of(TODAY, LocalTime.of(9, 30)))
        ));

        List<Post> timeline = contentService.getWall(TEST_USER_NAME);

        assertThat(timeline).extracting(Post::getAuthor, Post::getMessage)
                .containsExactly(
                        tuple(TEST_USER_NAME, "Message1"),
                        tuple(TEST_USER_NAME, "Message3"),
                        tuple(TEST_USER_NAME, "Message2")
                );
    }

    @Test
    public void shouldThrowExceptionWhenDisplayingWallOfNonExistentUser() {
        when(userService.findUser(TEST_USER_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contentService.getWall(TEST_USER_NAME))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User " + TEST_USER_NAME + " doesn't exist");
    }

    @Test
    public void shouldThrowExceptionWhenDisplayingTimelineOfNonExistentUser() {
        when(userService.findUser(TEST_USER_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contentService.getTimeline(TEST_USER_NAME))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User " + TEST_USER_NAME + " doesn't exist");
    }

    private static User user(String userName, User... followedUsers) {
        User user = new User(userName);
        Stream.of(followedUsers).forEach(user::follow);
        return user;
    }
}