package com.carlosobispo.postconsulting.repositories;

import com.carlosobispo.postconsulting.models.Post;
import com.carlosobispo.postconsulting.models.User;

import java.util.List;

public interface PostRepository extends BaseRepository<Post> {

    List<Post> findByOrderByCreatedAtDesc();

    List<Post> findByUserOrderByCreatedAtDesc(User user);

    Post findByUserAndId(User user, Long id);

}
