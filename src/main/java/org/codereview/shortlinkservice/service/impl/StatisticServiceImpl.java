package org.codereview.shortlinkservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codereview.shortlinkservice.domain.ShortLinkEntity;
import org.codereview.shortlinkservice.domain.repository.ShortLinkRepository;
import org.codereview.shortlinkservice.dto.ShortLinkDto;
import org.codereview.shortlinkservice.service.StatisticService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Сервис для ведения и работы со статистикой
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final ShortLinkRepository repository;

    /**
     * Увеличить количество переходов по ссылке на 1
     *
     * @param shortLink короткая ссылка
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void incrementCountFollowingLink(String shortLink) {
        log.info("increment count transition by link: {}", shortLink);
        repository.incrementCountByLink(shortLink);
    }

    /**
     * Получить статистку по короткой ссылке
     *
     * @param shortLink короткая ссылка
     * @return статистика
     * @throws NoSuchElementException если не найдена короткая ссылка
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ShortLinkDto getShortLinkStat(String shortLink) {
        log.info("get statistic for link: {}", shortLink);
        var shortLinkEntity = repository.findByLink(shortLink).orElseThrow();
        long rank = repository.getRankByLink(shortLink);

        return ShortLinkDto.builder()
                .link(shortLinkEntity.getLink())
                .original(shortLinkEntity.getOriginal())
                .count(shortLinkEntity.getCount())
                .rank(rank)
                .build();
    }

    /**
     * Получить статистку постранично
     *
     * @param page  номер страницы(начинается с 1)
     * @param count количество записей на странице(от 1 до 100)
     * @return статистика
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<ShortLinkDto> getStatisticByPage(int page, int count) {
        log.info("get statistic for page: {}, and count: {}", page, count);
        if (page <= 0 || count > 100 || count <= 0) {
            log.info("Illegal arguments");
            throw new IllegalArgumentException("проверьте аргументы page: " + page + " и count: " + count);
        }

        Pageable pageRequest = PageRequest.of(page - 1, count, Sort.by("count").descending());
        Page<ShortLinkEntity> shortLinkEntitiesForPage = repository.findAll(pageRequest);

        var startRank = ((page - 1) * count) + 1;
        var statisticForPage = new ArrayList<ShortLinkDto>();
        for (ShortLinkEntity shortLinkEntity : shortLinkEntitiesForPage.getContent()) {
            statisticForPage.add(ShortLinkDto.builder()
                    .link(shortLinkEntity.getLink())
                    .original(shortLinkEntity.getOriginal())
                    .count(shortLinkEntity.getCount())
                    .rank(startRank++)
                    .build());
        }
        return statisticForPage;
    }
}
