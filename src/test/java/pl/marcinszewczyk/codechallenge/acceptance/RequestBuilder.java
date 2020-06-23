package pl.marcinszewczyk.codechallenge.acceptance;

import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RequestBuilder {
    private final MockMvc mockMvc;
    private String activeUser;

    public RequestBuilder(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public static RequestBuilder request(MockMvc mvc) {
        return new RequestBuilder(mvc);
    }

    public RequestBuilder asAUser(String activeUser) {
        this.activeUser = activeUser;
        return this;
    }

    public ResultActions postMessage(String message) throws Exception {
        Assertions.assertNotNull(activeUser, "Setup error, specify user which performs a request");
        return mockMvc.perform(post("/post")
                .queryParam("currentUser", this.activeUser).content(message));
    }

    public ResultActions followUser(String followedUser) throws Exception {
        Assertions.assertNotNull(activeUser, "Setup error, specify user which performs a request");
        return mockMvc.perform(post(String.format("/users/%s/follow", followedUser))
                .queryParam("currentUser", activeUser));
    }

    public ResultActions getWallOfUser(String user) throws Exception {
        return mockMvc.perform(get(String.format("/users/%s/wall", user)));
    }

    public ResultActions getTimelineOfUser(String user) throws Exception {
        return mockMvc.perform(get(String.format("/users/%s/timeline", user)));
    }
}
