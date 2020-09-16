package com.cyan.amescua.services;

import com.cyan.amescua.model.AnalysedFeed;
import com.cyan.amescua.providers.FeedRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceTest {

    private static List<String> urls = new ArrayList<String>(){{
        add("https://news.google.com/rss?cf=all&hl=en-US&pz=1&gl=US&ceid=US:en");
        add("https://www.api.org/news-policy-and-issues/news-feed");
    }};
    private static final long ID = 1;
    private static final String mockTopFeeds = "[\n" +
            "            {\n" +
            "                \"title\": \"API says infrastructure development and permitting reform will get people back to work, revitalize local economies\",\n" +
            "                \"link\": \"https://www.api.org/news-policy-and-issues/news/2020/06/05/api-says-infrastructure-development-permitting-reform-will-revitalize-local-econ\",\n" +
            "                \"pubDate\": \"Fri, 05 Jun 2020 10:58:14 -0400\"\n" +
            "            }]";

    @InjectMocks
    FeedService feedService;

    @Mock
    FeedRepository feedRepository;

    @Test
    public void retrieveRSS() throws Exception {
        when(feedRepository.save(any(AnalysedFeed.class))).thenReturn(new AnalysedFeed("param1", "param2", ID));
        Map<String, Object> res = feedService.retrieveRSS(urls);
        assertNotNull(res);
        assertNotNull(res.values().toArray()[0]);
    }

    @Test
    public void getFeedById() {
        when(feedRepository.findById(any(Long.class))).thenReturn(Optional.of(new AnalysedFeed("param1", mockTopFeeds, ID)));
        Map<String, Object> f = feedService.getFeedById(ID);
        assertNotNull(f);
        assertNotNull(f.values());
    }
}