-- DROP DATABASE blog;
-- CREATE DATABASE blog;

create table role
(
    id   SMALLSERIAL        NOT NULL,
    name varchar(20) UNIQUE NOT NULL,
    primary key (id)
);

-- DROP TABLE IF EXISTS "user";
CREATE TABLE IF NOT EXISTS "user"
(
    id         bigserial    NOT NULL,
    name       VARCHAR(20)  NOT NULL UNIQUE,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    create_on  TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_on  TIMESTAMPTZ  NULL,
    role_id    smallint     NOT NULL,
    PRIMARY KEY (id),
    foreign key (role_id) REFERENCES role (id)
);

-- DROP TABLE IF EXISTS "tag";
CREATE TABLE IF NOT EXISTS "tag"
(
    id        bigserial   NOT NULL,
    name      VARCHAR(50) NOT NULL,
    create_on TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_on TIMESTAMPTZ NULL,
    PRIMARY KEY (id)
);

-- DROP TABLE IF EXISTS "post_tags";
CREATE TABLE IF NOT EXISTS "post"
(
    id                 BIGSERIAL    NOT NULL,
    title              VARCHAR(250) NOT NULL,
    preview_attachment TEXT         NOT NULL,
    user_id            BIGINT,
    create_on          TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    text               TEXT         NOT NULL,
    update_on          TIMESTAMPTZ  NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE SET NULL
);

-- DROP TABLE IF EXISTS "post";
create table post_tags
(
    post_id BIGINT NOT NULL,
    tags_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE,
    FOREIGN KEY (tags_id) REFERENCES tag (id) ON DELETE CASCADE
);

-- DROP TABLE IF EXISTS "comment";
CREATE TABLE IF NOT EXISTS "comment"
(
    id        BIGSERIAL   NOT NULL,
    text      TEXT        NOT NULL,
    post_id   BIGINT      NOT NULL,
    user_id   BIGINT,
--     tags_id   BIGINT      NOT NULL,
    create_on TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_on TIMESTAMPTZ NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES "post" (id)
);



-- DROP TABLE IF EXISTS "comment_tags";
-- create table comment_tags
-- (
--     comment_id int not null unique,
--     tags_id    int not null,
--     FOREIGN KEY (tags_id) REFERENCES tag (id)
-- );


-- alter table if exists post
--     add constraint post_tags foreign key (tags_id) references "post_tags" (post_id);
-- alter table if exists comment
--     add constraint comment_tags foreign key (tags_id) references "comment_tags" (comment_id);
