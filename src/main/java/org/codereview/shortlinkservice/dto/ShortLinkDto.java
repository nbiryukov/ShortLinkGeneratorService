package org.codereview.shortlinkservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto сущности ShortLinkEntity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkDto {

    private String link;
    private String original;
    private int count;
    private long rank;
}
