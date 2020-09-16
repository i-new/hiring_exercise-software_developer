package com.cyan.amescua.providers;

import com.cyan.amescua.model.AnalysedFeed;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@DataJpaTest
public class FeedRepositoryTest {

    @Autowired
    FeedRepository feedRepository;

    private static final long ID = 1;
    private static final String FIELD = "field1";

    @Test
    public void injectedComponentAreNotNull() {
        Assert.assertNotNull("it should not be null", feedRepository);
    }

    @Test
    public void givenAnalysedData_whenSave_thenOk() {
        AnalysedFeed analysedFeed = new AnalysedFeed("field1", "field2");
        feedRepository.save(analysedFeed);

        Optional<AnalysedFeed> analysedFeed1 = feedRepository.findById(ID);
        assertNotNull("the object was created", analysedFeed1.isPresent());
        assertEquals("the name is correct", FIELD, analysedFeed1.get().getHotTopics());
    }
}
