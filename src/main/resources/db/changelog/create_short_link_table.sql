CREATE TABLE short_link
(
    id       uuid primary key not null,
    link     varchar          not null unique,
    original varchar          not null,
    count    int              not null,
    constraint positive_or_zero_count CHECK (short_link.count >= 0)
)
