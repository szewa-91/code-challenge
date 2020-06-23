package pl.marcinszewczyk.codechallenge.infrastructure;

import org.springframework.stereotype.Component;
import pl.marcinszewczyk.codechallenge.content.Post;
import pl.marcinszewczyk.codechallenge.content.PostRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class InMemoryPostRepository implements PostRepository {
    Collection<Post> storage = new ArrayList<>();

    @Override
    public Collection<Post> getByAuthor(String userName) {
        return storage.stream().filter(post -> post.getAuthor().equals(userName)).collect(Collectors.toList());
    }

    @Override
    public Post save(Post post) {
        storage.add(post);
        return post;
    }
}
