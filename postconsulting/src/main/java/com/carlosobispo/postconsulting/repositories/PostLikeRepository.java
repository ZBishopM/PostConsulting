package com.carlosobispo.postconsulting.repositories;

import com.carlosobispo.postconsulting.models.PostLike;
import com.carlosobispo.postconsulting.models.User;

import java.util.List;

public interface PostLikeRepository extends BaseRepository<PostLike> {
    List<PostLike> findByUserOrderByCreatedAtDesc(User user);
}
