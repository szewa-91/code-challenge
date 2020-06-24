package pl.marcinszewczyk.codechallenge.content;

import org.springframework.stereotype.Service;
import pl.marcinszewczyk.codechallenge.user.User;
import pl.marcinszewczyk.codechallenge.user.UserNotFoundException;
import pl.marcinszewczyk.codechallenge.user.UserService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@Service
public class ContentService {
    private final UserService userService;
    private final PostRepository postRepository;

    public ContentService(UserService userService, PostRepository postRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
    }

    public void post(String userName, String message) {
        userService.findOrCreateUser(userName);
        postRepository.save(new Post(message, userName));
    }

    public List<Post> getTimeline(String userName) {
        return getNamesOfFollowedUsers(userName)
                .map(this::getPostsByAuthor)
                .flatMap(Collection::stream)
                .sorted(byCreateTimeReversed())
                .collect(Collectors.toList());
    }

    public List<Post> getWall(String userName) {
        getUserOrThrowException(userName);

        return getPostsByAuthor(userName);
    }

    public List<Post> getPostsByAuthor(String userName) {
        return postRepository.getByAuthor(userName).stream()
                .sorted(byCreateTimeReversed())
                .collect(Collectors.toList());
    }

    private Stream<String> getNamesOfFollowedUsers(String userName) {
        User user = getUserOrThrowException(userName);
        return user.getFollowed().stream()
                .map(User::getName);
    }

    private User getUserOrThrowException(String userName) {
        return userService.findUser(userName).orElseThrow(() -> new UserNotFoundException(userName));
    }

    private static Comparator<Post> byCreateTimeReversed() {
        return comparing(Post::getCreated).reversed();
    }
}
