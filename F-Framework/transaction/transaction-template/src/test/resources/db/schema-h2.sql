SET MODE=MYSQL;

DROP TABLE IF EXISTS test;

CREATE TABLE test
(
    id          int(11)  NOT NULL AUTO_INCREMENT ,
    title       varchar(80) NOT NULL,
    author      varchar(32) NOT NULL,
    create_time datetime   NOT NULL,
    update_time datetime   NOT NULL,
    PRIMARY KEY(`id`)
);

DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id          int(11)  NOT NULL AUTO_INCREMENT ,
    name       varchar(80) NOT NULL,
    age      tinyint(4) NOT NULL,
    create_time datetime   NOT NULL,
    update_time datetime   NOT NULL,
    PRIMARY KEY(`id`)
);
