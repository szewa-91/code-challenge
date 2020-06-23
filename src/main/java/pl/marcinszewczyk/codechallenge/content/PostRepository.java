package pl.marcinszewczyk.codechallenge.content;

import java.util.Collection;

public interface PostRepository {
    Collection<Post> getByAuthor(String userName);

    Post save(Post post);
}
