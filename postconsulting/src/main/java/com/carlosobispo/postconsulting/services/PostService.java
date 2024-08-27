package com.carlosobispo.postconsulting.services;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.List;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import com.carlosobispo.postconsulting.models.Post;
import com.carlosobispo.postconsulting.models.User;
import com.carlosobispo.postconsulting.repositories.PostRepository;

@Service
public class PostService extends BaseService<Post> {
    private final PostRepository postRepository;
    private final DateTimeFormatter formatter;

    public PostService(PostRepository postRepository) {
        super(postRepository);
        this.postRepository = postRepository;
        this.formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy 'a las' h:mm a")
        .withLocale(Locale.forLanguageTag("es-Es"))
        .withZone(ZoneId.systemDefault());
    }

    public List<Post> findByAuthor(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    //TODO: findByLikedByUser
    public List<Post> findByLikedByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Post save(Post post) {
        post.setPostedDateFormatted(formatter.format(Instant.now()));
        return postRepository.save(post);
    }

    public List<Post> findByUsername(String email) {
        return postRepository.findByUserEmail(email);
    }

    public List<Post> countStats(List<Post> posts){
        
        for (Post post : posts) {
            post.setCountLikes(post.getPostLikes().size());
            post.setCountComments(post.getComments().size());
            post.setPostedDateFormatted(formatter.format(post.getCreatedAt()));
        }
        return posts;
    }
}
