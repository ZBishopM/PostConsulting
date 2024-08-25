package com.carlosobispo.postconsulting.services;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.carlosobispo.postconsulting.repositories.BaseRepository;

@Service
public abstract class BaseService<T> {
    private final BaseRepository<T> repository;

    public BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }
    public T save(T entity){
        return repository.save(entity);
    }

    public T findById(Long id){
        Optional<T> optional = repository.findById(id);
        return optional.isPresent()?optional.get():null;
    }
    
    public void update(T entity){
        repository.save(entity);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }
}
