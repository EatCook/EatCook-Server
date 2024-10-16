CREATE TABLE archive
(
    archive_id       BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    last_modified_at datetime NULL,
    user_id          BIGINT NULL,
    post_id          BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (archive_id)
);

CREATE TABLE follow
(
    from_user BIGINT NOT NULL,
    to_user   BIGINT NULL
);

CREATE TABLE food_ingredients
(
    post_id         BIGINT NOT NULL,
    ingredient_name VARCHAR(255) NULL
);

CREATE TABLE hibernate_sequence
(
    next_val BIGINT NULL
);

CREATE TABLE itcook_user
(
    user_id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at         datetime NULL,
    last_modified_at   datetime NULL,
    badge              VARCHAR(255) NULL,
    device_token       VARCHAR(255) NULL,
    email              VARCHAR(255) NULL,
    event_alert_type   VARCHAR(255) NULL,
    life_type          VARCHAR(255) NULL,
    nick_name          VARCHAR(255) NULL,
    password           VARCHAR(255) NULL,
    profile            VARCHAR(255) NULL,
    provider_type      VARCHAR(255) NOT NULL,
    service_alert_type VARCHAR(255) NULL,
    user_role          VARCHAR(255) NOT NULL,
    user_state         VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_id)
);

CREATE TABLE liked
(
    liked_id BIGINT AUTO_INCREMENT NOT NULL,
    user_id  BIGINT NULL,
    post_id  BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (liked_id)
);

CREATE TABLE notification
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    created_at        datetime NULL,
    last_modified_at  datetime NULL,
    checked           BIT(1) NULL,
    message           VARCHAR(255) NULL,
    notification_type VARCHAR(255) NULL,
    post_id           BIGINT NULL,
    title             VARCHAR(255) NULL,
    user_id           BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE post
(
    post_id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at       datetime NULL,
    last_modified_at datetime NULL,
    introduction     VARCHAR(255) NULL,
    post_flag        VARCHAR(255) NULL,
    post_image_path  VARCHAR(255) NULL,
    recipe_name      VARCHAR(255) NULL,
    recipe_time      INT NULL,
    user_id          BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (post_id)
);

CREATE TABLE post_cooking_theme
(
    cooking_theme_id BIGINT AUTO_INCREMENT NOT NULL,
    cooking_type     VARCHAR(255) NULL,
    post_id          BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (cooking_theme_id)
);

CREATE TABLE post_life_type
(
    post_id   BIGINT NOT NULL,
    life_type VARCHAR(255) NULL
);

CREATE TABLE recipe_process
(
    recipe_process_id         BIGINT AUTO_INCREMENT NOT NULL,
    recipe_process_image_path VARCHAR(255) NULL,
    recipe_writing            VARCHAR(255) NOT NULL,
    step_num                  INT          NOT NULL,
    post_id                   BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (recipe_process_id)
);

CREATE TABLE review
(
    review_id        BIGINT NOT NULL,
    created_at       datetime NULL,
    last_modified_at datetime NULL,
    comment          VARCHAR(255) NULL,
    post_id          BIGINT NULL,
    user_id          BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (review_id)
);

CREATE TABLE user_cooking_theme
(
    user_cooking_theme_id BIGINT AUTO_INCREMENT NOT NULL,
    cooking_type          VARCHAR(255) NULL,
    user_id               BIGINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (user_cooking_theme_id)
);

ALTER TABLE post_life_type
    ADD CONSTRAINT FK17qexc3th2x9vpc88h1n9xqeu FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK17qexc3th2x9vpc88h1n9xqeu ON post_life_type (post_id);

ALTER TABLE recipe_process
    ADD CONSTRAINT FK4v24ha5bsxaua3ujuuagdjkdd FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK4v24ha5bsxaua3ujuuagdjkdd ON recipe_process (post_id);

ALTER TABLE post_cooking_theme
    ADD CONSTRAINT FK5h9i8w17kdufbs12j7o9a4461 FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

CREATE INDEX FK5h9i8w17kdufbs12j7o9a4461 ON post_cooking_theme (post_id);

ALTER TABLE follow
    ADD CONSTRAINT FK78a6mhikc5hrtn6a7etbf3rma FOREIGN KEY (from_user) REFERENCES itcook_user (user_id) ON DELETE NO ACTION;

CREATE INDEX FK78a6mhikc5hrtn6a7etbf3rma ON follow (from_user);

ALTER TABLE user_cooking_theme
    ADD CONSTRAINT FKc9pdsov0xldc798lc3sul8t9g FOREIGN KEY (user_id) REFERENCES itcook_user (user_id) ON DELETE NO ACTION;

CREATE INDEX FKc9pdsov0xldc798lc3sul8t9g ON user_cooking_theme (user_id);

ALTER TABLE food_ingredients
    ADD CONSTRAINT FKf948wyt3at8o98k5thdxkqljq FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE NO ACTION;

CREATE INDEX FKf948wyt3at8o98k5thdxkqljq ON food_ingredients (post_id);