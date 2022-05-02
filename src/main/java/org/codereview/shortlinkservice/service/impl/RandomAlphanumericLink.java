package org.codereview.shortlinkservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.codereview.shortlinkservice.service.ShortLinkGenerator;
import org.springframework.stereotype.Component;

/**
 * Генератор короткой ссылки, основанный на рандомной генерации цифр и букв
 */
@Component
public class RandomAlphanumericLink implements ShortLinkGenerator {

    private final int GENERATED_STRING_LENGTH = 7;

    /**
     * Генерация ссылки
     *
     * @param original оригинальная ссылка
     * @return короткая ссылка
     */
    @Override
    public String generateShortLink(String original) {
        return RandomStringUtils.randomAlphanumeric(GENERATED_STRING_LENGTH) + original.length();
    }
}
