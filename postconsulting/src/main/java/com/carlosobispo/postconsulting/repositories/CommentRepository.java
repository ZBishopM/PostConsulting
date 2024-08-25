package com.carlosobispo.postconsulting.repositories;

import com.carlosobispo.postconsulting.models.Comment;
import com.carlosobispo.postconsulting.models.Post;

import java.util.List;

public interface CommentRepository extends BaseRepository<Comment> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
}
