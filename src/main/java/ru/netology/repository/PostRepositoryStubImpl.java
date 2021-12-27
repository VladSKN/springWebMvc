package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepositoryStubImpl implements PostRepository {

    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    public List<Post> all() {
        if (posts.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = id.incrementAndGet();
            post = new Post(newId, post.getContent());
            posts.put(post.getId(), post);
        }
        if (posts.containsKey(post.getId()) && post.isDeleted()) {
            posts.put(post.getId(), post);
        } else {
            throw new NotFoundException();
        }
        return post;
    }

    public void removeById(long id) {
        posts.get(id).setDeleted(true);
        //posts.remove(id);
    }
}
