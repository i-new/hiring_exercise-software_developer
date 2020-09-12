package com.cyan.amescua.providers;

import com.cyan.amescua.model.AnalysedFeed;
import org.springframework.data.repository.CrudRepository;


public interface FeedRepository extends CrudRepository<AnalysedFeed, Long> {

}
