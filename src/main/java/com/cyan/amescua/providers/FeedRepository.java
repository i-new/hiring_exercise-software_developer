package com.cyan.amescua.providers;

import com.cyan.amescua.model.Feed;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedRepository extends CrudRepository<Feed, Long> {

    Feed findById(String id);

    List<Feed> findByDescriptionContaining(String id);
}
