package org.codereview.shortlinkservice.service;

public interface ShortLinkService {

    String createShortLink(String originalLink);

    String getOriginalLink(String shortLink);
}
