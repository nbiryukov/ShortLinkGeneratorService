package org.codereview.shortlinkservice.domain.repository;

import org.codereview.shortlinkservice.domain.ShortLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с БД
 */
public interface ShortLinkRepository extends JpaRepository<ShortLinkEntity, UUID> {

    Optional<ShortLinkEntity> findByLink(String link);


    @Modifying
    @Query("update ShortLinkEntity l set l.count = l.count + 1 where l.link = :link")
    void incrementCountByLink(String link);


    @Query(value = "select rank_number from "
            + " (select link, rank() over (order by count desc) rank_number from short_link) as short_link_rank"
            + " where link = :link", nativeQuery = true)
    long getRankByLink(@Param("link") String link);
}
