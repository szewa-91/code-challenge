package pl.marcinszewczyk.codechallenge.content;

import java.util.Collection;

public interface PostRepository {
    Collection<Post> getByAuthor(String userName);

    void save(Post post);
}
