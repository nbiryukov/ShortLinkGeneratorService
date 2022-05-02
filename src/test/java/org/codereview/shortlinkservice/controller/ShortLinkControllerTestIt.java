package org.codereview.shortlinkservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codereview.shortlinkservice.IntegrationTestBase;
import org.codereview.shortlinkservice.domain.ShortLinkEntity;
import org.codereview.shortlinkservice.domain.repository.ShortLinkRepository;
import org.codereview.shortlinkservice.dto.RequestGenerateShortLinkDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShortLinkControllerTestIt extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShortLinkRepository shortLinkRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void clear() {
        shortLinkRepository.deleteAll();
    }


    @Test
    public void testGenerateNewLinkSuccess() throws Exception {
        var request = new RequestGenerateShortLinkDto();
        request.setOriginal("originalurl");
        this.mockMvc.perform(post("/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.link", is(any(String.class))));
    }

    @Test
    public void testRedirectSuccess() throws Exception {
        var link = "123";
        var original = "https://www.google.com";
        var expectedCount = 1;
        var shortLinkEntity = new ShortLinkEntity();
        shortLinkEntity.setId(UUID.randomUUID());
        shortLinkEntity.setLink(link);
        shortLinkEntity.setOriginal(original);
        shortLinkEntity.setCount(0);
        shortLinkRepository.save(shortLinkEntity);


        this.mockMvc.perform(get("/l/" + link))
                .andExpect(redirectedUrl(original))
                .andExpect(status().isFound());

        assertEquals(expectedCount, (int) shortLinkRepository.findByLink(link).get().getCount());
    }

    @Test
    public void testRedirectNotFound() throws Exception {
        var link = "123";

        this.mockMvc.perform(get("/l/" + link))
                .andExpect(status().isNotFound());
        System.out.println(shortLinkRepository.findAll().size());
    }
}
