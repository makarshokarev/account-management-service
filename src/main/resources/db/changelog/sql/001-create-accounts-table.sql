CREATE TABLE account
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    phone_nr      VARCHAR(20),
    created_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_time  TIMESTAMP,
    is_active     BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_accounts PRIMARY KEY (id),
    CONSTRAINT uk_accounts_phone_nr_is_active UNIQUE (phone_nr, is_active)
);
