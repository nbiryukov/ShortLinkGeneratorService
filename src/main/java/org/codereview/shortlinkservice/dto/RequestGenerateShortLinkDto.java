package org.codereview.shortlinkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto запроса на создание короткой ссылки
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestGenerateShortLinkDto {

    private String original;
}
