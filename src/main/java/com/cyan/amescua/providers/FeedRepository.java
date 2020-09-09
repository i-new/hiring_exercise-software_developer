package com.cyan.amescua.providers;

import com.cyan.amescua.model.Feed;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public class FeedRepository implements CrudRepository<Feed, Long> {


    @Override
    public <S extends Feed> S save(S s) {
        return null;
    }

    @Override
    public <S extends Feed> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Feed> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Feed> findAll() {
        return null;
    }

    @Override
    public Iterable<Feed> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Feed feed) {

    }

    @Override
    public void deleteAll(Iterable<? extends Feed> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
