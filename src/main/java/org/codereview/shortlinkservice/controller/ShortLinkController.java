package org.codereview.shortlinkservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codereview.shortlinkservice.service.ShortLinkService;
import org.codereview.shortlinkservice.service.StatisticService;
import org.codereview.shortlinkservice.util.UtilPrefix;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Контроллер управления короткими ссылками
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ShortLinkController {

    private static final String GENERATE_ORIGINAL_FIELD = "original";
    private static final String GENERATE_LINK_FIELD = "link";


    private final ShortLinkService shortLinkService;
    private final StatisticService statisticService;


    /**
     * Создание новой короткой ссылки
     *
     * @param originalLink оригинальная ссылка
     * @return json объект с ключом "link"
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateShortLink(@RequestBody Map<String, String> originalLink) {
        log.info("request generate new short link");
        var original = originalLink.get(GENERATE_ORIGINAL_FIELD);
        if (original == null) {
            log.info("wrong request");
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.BAD_REQUEST);
        }

        var shortLink = shortLinkService.createShortLink(original);

        var responseMap = new HashMap<String, String>();
        responseMap.put(GENERATE_LINK_FIELD, UtilPrefix.PREFIX_URL + shortLink);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    /**
     * Найти оригинальную ссылку и совершить переход
     *
     * @param shortLink короткая ссылка
     * @param response  HttpServletResponse
     */
    @GetMapping(UtilPrefix.PREFIX_URL + "{shortLink}")
    public void redirect(@PathVariable String shortLink, HttpServletResponse response) {
        log.info("redirect by link: {}", shortLink);
        try {
            String originalLink = shortLinkService.getOriginalLink(shortLink);
            statisticService.incrementCountFollowingLink(shortLink);
            response.sendRedirect(originalLink);
        } catch (NoSuchElementException e) {
            log.info("not found original by link: {}", shortLink);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IOException e) {
            log.error("redirect exception: ", e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
