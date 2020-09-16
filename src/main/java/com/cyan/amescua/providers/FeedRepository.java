package com.cyan.amescua.providers;

import com.cyan.amescua.model.AnalysedFeed;
import org.springframework.data.repository.CrudRepository;

/**
 * Database repository, it stores and retrieves Feeds data from the H2 database.
 */
public interface FeedRepository extends CrudRepository<AnalysedFeed, Long> {

}
