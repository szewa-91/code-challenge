package pl.marcinszewczyk.codechallenge.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.marcinszewczyk.codechallenge.acceptance.RequestBuilder.request;

@SpringBootTest
@AutoConfigureMockMvc
public class AcceptanceTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    InMemoryUserRepository userRepository;
    @Autowired
    InMemoryPostRepository postRepository;

    @BeforeEach
    public void setUp() {
        userRepository.clear();
        postRepository.clear();
    }

    @Test
    public void shouldDisplayWallOfAUser() throws Exception {
        request(mockMvc).asAUser("user1").postMessage("Message1");
        request(mockMvc).asAUser("user2").postMessage("Message2");
        request(mockMvc).asAUser("user1").postMessage("Message3");

        String user1Wall = request(mockMvc).getWallOfUser("user1")
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
        request(mockMvc).asAUser("user1").postMessage("Message1");
        request(mockMvc).asAUser("user2").postMessage("Message2");
        request(mockMvc).asAUser("user1").postMessage("Message3");
        request(mockMvc).asAUser("user3").postMessage("Message4");

        request(mockMvc).asAUser("follower").postMessage("Not for timeline");
        request(mockMvc).asAUser("follower").followUser("user1");
        request(mockMvc).asAUser("follower").followUser("user2");
        request(mockMvc).asAUser("follower").followUser("user3");


        String user1Wall = request(mockMvc).getTimelineOfUser("follower")
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
        request(mockMvc).getWallOfUser("user")
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAnswerWithNotFoundIfNoUserAndRequestingTimeLine() throws Exception {
        request(mockMvc).getTimelineOfUser("user")
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAnswerWithNotFoundIfTryingToFollowUserThatDoesNotExists() throws Exception {
        request(mockMvc).asAUser("follower").followUser("user")
                .andExpect(status().isNotFound());
    }

    private List<Post> deserialize(String response) throws JsonProcessingException {
        return new ObjectMapper().readValue(response, new TypeReference<List<Post>>() {});
    }
}
