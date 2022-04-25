package org.codereview.shortlinkservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codereview.shortlinkservice.dto.ShortLinkDto;
import org.codereview.shortlinkservice.service.impl.StatisticServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Контроллер для статистики
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticServiceImpl statisticService;

    /**
     * Получение статистики по короткой ссылке
     *
     * @param shortLink короткая ссылка
     * @return статистика
     */
    @GetMapping("/stats/{shortLink}")
    public ResponseEntity<ShortLinkDto> getStatsByShortLink(@PathVariable String shortLink) {
        log.info("request get statistic for link: {}", shortLink);
        try {
            return new ResponseEntity<>(statisticService.getShortLinkStat(shortLink), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.info("not found statistic by link: {}", shortLink);
            return new ResponseEntity<>(new ShortLinkDto(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получение статистики постранично
     *
     * @param page  номер страницы
     * @param count количество записей на странице
     * @return статистика
     */
    @GetMapping("/stats")
    public ResponseEntity<List<ShortLinkDto>> getStatsByPage(@RequestParam @Min(1) int page,
                                                             @RequestParam @Min(1) @Max(100) int count) {
        log.info("request get statistic for page: {} and count: {}", page, count);
        try {
            return new ResponseEntity<>(statisticService.getStatisticByPage(page, count), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.info("bag request");
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }
}
