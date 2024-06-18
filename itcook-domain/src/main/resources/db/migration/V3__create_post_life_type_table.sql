-- auto-generated definition
create table post_life_type
(
    post_id   bigint not null,
    life_type int    null,
    constraint fk_post_life_type_post_id
        foreign key (post_id) references post (post_id)
);