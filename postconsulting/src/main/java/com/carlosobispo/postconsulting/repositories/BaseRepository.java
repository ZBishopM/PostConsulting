package com.carlosobispo.postconsulting.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import lombok.NonNull;

@NoRepositoryBean
public interface BaseRepository<T> extends CrudRepository<T, Long> {
    @SuppressWarnings("null")
    @NonNull
    List<@NonNull T> findAll();
}
