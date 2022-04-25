package org.codereview.shortlinkservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.util.UUID;

@Entity
@Table(name = "short_link")
@Data
@NoArgsConstructor
public class ShortLinkEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column
    private UUID id;

    @Column
    @NotBlank
    private String link;

    @Column
    @NotBlank
    private String original;

    @Column
    @NotNull
    @PositiveOrZero
    private Integer count;
}
