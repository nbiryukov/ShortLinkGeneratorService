package org.codereview.shortlinkservice.service;

import org.codereview.shortlinkservice.dto.ShortLinkDto;

import java.util.List;

public interface StatisticService {

    void incrementCountFollowingLink(String shortLink);

    ShortLinkDto getShortLinkStat(String shortLink);

    List<ShortLinkDto> getStatisticByPage(int page, int count);
}
