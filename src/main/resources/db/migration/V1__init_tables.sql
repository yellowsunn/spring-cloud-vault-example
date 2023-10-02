create table comment
(
    id      bigint       not null auto_increment,
    post_id bigint       not null,
    name    varchar(255) not null,
    email   varchar(100) not null,
    body    varchar(512) not null,
    primary key (id)
);
