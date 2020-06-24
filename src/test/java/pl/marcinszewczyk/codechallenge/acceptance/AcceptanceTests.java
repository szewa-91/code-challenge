package pl.marcinszewczyk.codechallenge.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pl.marcinszewczyk.codechallenge.content.Post;
import pl.marcinszewczyk.codechallenge.infrastructure.InMemoryPostRepository;
import pl.marcinszewczyk.codechallenge.infrastructure.InMemoryUserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.marcinszewczyk.codechallenge.acceptance.AcceptanceTestRequestBuilder.requestbuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    InMemoryUserRepository userRepository;
    @Autowired
    InMemoryPostRepository postRepository;

    @Test
    public void shouldDisplayWallOfAUser() throws Exception {
        request().asAUser("user1").postMessage("Message1");
        request().asAUser("user2").postMessage("Message2");
        request().asAUser("user1").postMessage("Message3");

        String user1Wall = request().getWallOfUser("user1")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(deserialize(user1Wall)).extracting(Post::getAuthor, Post::getMessage)
                .containsExactly(
                        tuple("user1", "Message3"),
                        tuple("user1", "Message1")
                );
    }

    @Test
    public void shouldDisplayTimelineOfFollowedUsers() throws Exception {
        request().asAUser("user1").postMessage("Message1");
        request().asAUser("user2").postMessage("Message2");
        request().asAUser("user1").postMessage("Message3");
        request().asAUser("user3").postMessage("Message4");

        request().asAUser("follower").postMessage("Not for timeline");
        request().asAUser("follower").followUser("user1");
        request().asAUser("follower").followUser("user2");
        request().asAUser("follower").followUser("user3");


        String user1Wall = request().getTimelineOfUser("follower")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(deserialize(user1Wall)).extracting(Post::getAuthor, Post::getMessage)
                .containsExactly(
                        tuple("user3", "Message4"),
                        tuple("user1", "Message3"),
                        tuple("user2", "Message2"),
                        tuple("user1", "Message1")
                );
    }

    @Test
    public void shouldAnswerWithNotFoundIfNoUserAndRequestingWall() throws Exception {
        request().getWallOfUser("user")
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAnswerWithNotFoundIfNoUserAndRequestingTimeLine() throws Exception {
        request().getTimelineOfUser("user")
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAnswerWithNotFoundIfTryingToFollowUserThatDoesNotExists() throws Exception {
        request().asAUser("follower").followUser("user")
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAnswerWithBadRequestIfTheMessageIsTooLong() throws Exception {
        request().asAUser("user").postMessage(stringOfLength(141))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAnswerWithBadRequestIfTheMessageIsEmpty() throws Exception {
        request().asAUser("user").postMessage("")
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    public void setUp() {
        userRepository.clear();
        postRepository.clear();
    }

    private List<Post> deserialize(String response) throws JsonProcessingException {
        return new ObjectMapper().readValue(response, new TypeReference<>() {
        });
    }

    private AcceptanceTestRequestBuilder request() {
        return requestbuilder(mockMvc);
    }

    private static String stringOfLength(int chars) {
        return IntStream.range(0, chars).mapToObj(i -> "a").collect(Collectors.joining());
    }
}
