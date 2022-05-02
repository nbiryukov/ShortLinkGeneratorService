package org.codereview.shortlinkservice.controller;

import org.codereview.shortlinkservice.IntegrationTestBase;
import org.codereview.shortlinkservice.domain.ShortLinkEntity;
import org.codereview.shortlinkservice.domain.repository.ShortLinkRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatisticControllerTestIt extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShortLinkRepository shortLinkRepository;

    @Before
    public void clear() {
        shortLinkRepository.deleteAll();
    }

    @Test
    public void testStatisticRank() throws Exception {
        var link = "123";
        var expectedRank = 2;
        var shortLinkEntity = new ShortLinkEntity();
        shortLinkEntity.setId(UUID.randomUUID());
        shortLinkEntity.setLink(link);
        shortLinkEntity.setOriginal("https://www.google.com");
        shortLinkEntity.setCount(10);
        shortLinkRepository.save(shortLinkEntity);

        var shortLinkEntityTwo = new ShortLinkEntity();
        shortLinkEntityTwo.setId(UUID.randomUUID());
        shortLinkEntityTwo.setLink("link");
        shortLinkEntityTwo.setOriginal("https://www.google.ru");
        shortLinkEntityTwo.setCount(15);
        shortLinkRepository.save(shortLinkEntityTwo);


        this.mockMvc.perform(get("/stats/" + link))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rank", is(expectedRank)))
                .andExpect(jsonPath("$.link", is(link)));
    }

    @Test
    public void testStatisticRankNotFound() throws Exception {
        var link = "123";

        this.mockMvc.perform(get("/stats/" + link))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testStatisticRankPage() throws Exception {
        var shortLinkEntity = new ShortLinkEntity();
        shortLinkEntity.setId(UUID.randomUUID());
        shortLinkEntity.setLink("123");
        shortLinkEntity.setOriginal("https://www.google.com");
        shortLinkEntity.setCount(10);
        shortLinkRepository.save(shortLinkEntity);

        var shortLinkEntityTwo = new ShortLinkEntity();
        shortLinkEntityTwo.setId(UUID.randomUUID());
        shortLinkEntityTwo.setLink("link");
        shortLinkEntityTwo.setOriginal("https://www.google.ru");
        shortLinkEntityTwo.setCount(15);
        shortLinkRepository.save(shortLinkEntityTwo);


        this.mockMvc.perform(get("/stats/?page=1&count=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].link", is("link")))
                .andExpect(jsonPath("$.[0].rank", is(1)))
                .andExpect(jsonPath("$.[1].link", is("123")))
                .andExpect(jsonPath("$.[1].rank", is(2)));
    }

    @Test
    public void testStatisticRankPageBadRequest() throws Exception {

        this.mockMvc.perform(get("/stats/?page=-1&count=10"))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(get("/stats/?page=1&count=1000"))
                .andExpect(status().isBadRequest());
    }
}
