-- auto-generated definition
create table notification
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6)  null,
    last_modified_at  datetime(6)  null,
    checked           bit          null,
    message           varchar(255) null,
    notification_type varchar(255) null,
    title             varchar(255) null,
    user_id           bigint       null,
    post_id           bigint       null
);

