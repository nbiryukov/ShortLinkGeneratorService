package org.codereview.shortlinkservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codereview.shortlinkservice.domain.ShortLinkEntity;
import org.codereview.shortlinkservice.domain.repository.ShortLinkRepository;
import org.codereview.shortlinkservice.service.ShortLinkGenerator;
import org.codereview.shortlinkservice.service.ShortLinkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Сервис управления и работы с сылками
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl implements ShortLinkService {

    private final ShortLinkGenerator generator;
    private final ShortLinkRepository repository;


    /**
     * Создание новой короткой ссылки
     *
     * @param originalLink оригинальная ссылка
     * @return короткая ссылка
     */
    @Override
    @Transactional
    public String createShortLink(String originalLink) {

        log.info("create link for original {}", originalLink);

        var link = generator.generateShortLink(originalLink);

        var shortLink = new ShortLinkEntity();
        shortLink.setOriginal(originalLink);
        shortLink.setCount(0);
        shortLink.setLink(link);

        repository.save(shortLink);

        log.info("successful creation of a short link {} for the original link {}", link, originalLink);

        return shortLink.getLink();
    }

    /**
     * Получить оригинальную ссылку по короткой
     *
     * @param shortLink короткая ссылка
     * @return оригинальная ссылка
     * @throws NoSuchElementException если не найдена короткая ссылка
     */
    @Override
    @Transactional
    public String getOriginalLink(String shortLink) {
        log.info("get original by link: {}", shortLink);
        var shortLinkEntity = repository.findByLink(shortLink).orElseThrow();
        return shortLinkEntity.getOriginal();
    }
}
