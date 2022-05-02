package org.codereview.shortlinkservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codereview.shortlinkservice.dto.RequestGenerateShortLinkDto;
import org.codereview.shortlinkservice.dto.ResponseGenerateShortLinkDto;
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

    private final ShortLinkService shortLinkService;
    private final StatisticService statisticService;


    /**
     * Создание новой короткой ссылки
     *
     * @param request запрос с оригинальной ссылкой
     * @return ответ с короткой ссылкой
     */
    @PostMapping("/generate")
    public ResponseEntity<ResponseGenerateShortLinkDto> generateShortLink(@RequestBody RequestGenerateShortLinkDto request) {
        log.info("Request generate new short link by original link: {}", request);
        var original = request.getOriginal();

        var shortLink = shortLinkService.createShortLink(original);

        return new ResponseEntity<>(new ResponseGenerateShortLinkDto(UtilPrefix.PREFIX_URL + shortLink), HttpStatus.OK);
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
        } catch (IOException e) {
            log.error("redirect exception: ", e);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
