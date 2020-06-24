package pl.marcinszewczyk.codechallenge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.marcinszewczyk.codechallenge.content.ContentService;
import pl.marcinszewczyk.codechallenge.content.Post;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/users/{user}/timeline")
    public List<Post> getTimeline(@PathVariable String user) {
        return contentService.getTimeline(user);
    }

    @GetMapping("/users/{user}/wall")
    public List<Post> getWall(@PathVariable String user) {
        return contentService.getWall(user);
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestParam String activeUser, @RequestBody String message, HttpServletResponse response) {
        contentService.post(activeUser, message);
    }

}
