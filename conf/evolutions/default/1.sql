# --- !Ups

CREATE TABLE user (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255),
    mail varchar(255),
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE user;
