CREATE TABLE users
(
    id       int(11) NOT NULL AUTO_INCREMENT,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role     varchar(50) NOT NULL,
    created_at DATE DEFAULT NULL,
    updated_at DATE DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE task_lists
(
    id          int(11) NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    description varchar(255) DEFAULT NULL,
    user_id     int(11) NOT NULL,
    is_deleted  tinyint(1) NOT NULL DEFAULT '0',
    created_at DATE DEFAULT NULL,
    updated_at DATE DEFAULT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE token
(
    id         int(11) NOT NULL AUTO_INCREMENT,
    token      varchar(255) NOT NULL,
    token_type varchar(255) NOT NULL,
    user_id    int(11) NOT NULL,
    expired    tinyint(1) NOT NULL DEFAULT '0',
    revoked    tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE tokens
(
    id         int(11) NOT NULL AUTO_INCREMENT,
    value      varchar(255) NOT NULL,
    user_id    int(11) NOT NULL,
    expires_at timestamp    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE tasks
(
    id          int(11) NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    description varchar(255) DEFAULT NULL,
    status      varchar(255) DEFAULT NULL,
    is_deleted  tinyint(1) NOT NULL DEFAULT '0',
    created_at DATE DEFAULT NULL,
    updated_at DATE DEFAULT NULL,
    tasklist_id int(11) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (tasklist_id) REFERENCES task_lists (id)
);