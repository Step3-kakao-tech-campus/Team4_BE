CREATE SCHEMA IF NOT EXISTS `matgpt_db` DEFAULT CHARACTER SET utf8mb4;

USE `matgpt_db`;

CREATE TABLE category_tb (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             name VARCHAR(255) NOT NULL UNIQUE,
                             PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE coin_earning_history_tb (
                                         amount INTEGER NOT NULL,
                                         balance INTEGER NOT NULL,
                                         coin_id BIGINT,
                                         earned_at DATETIME(6),
                                         id BIGINT NOT NULL AUTO_INCREMENT,
                                         PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE coin_tb (
                         balance INTEGER NOT NULL,
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         user_id BIGINT,
                         PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE coin_usage_history_tb (
                                       amount INTEGER NOT NULL,
                                       balance INTEGER NOT NULL,
                                       coin_id BIGINT,
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       used_at DATETIME(6),
                                       PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE food_tb (
                         avg_rating FLOAT(53),
                         nums_of_review INTEGER,
                         created_at DATETIME(6) NOT NULL,
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         store_id BIGINT,
                         updated_at DATETIME(6) NOT NULL,
                         created_by VARCHAR(255),
                         food_description VARCHAR(255),
                         food_name VARCHAR(255) NOT NULL,
                         updated_by VARCHAR(255),
                         PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE gpt_guidance_tb (
                                 created_at DATETIME(6) NOT NULL,
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 user_id BIGINT,
                                 content VARCHAR(1000) NOT NULL,
                                 PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE gpt_review_tb (
                               last_nums_of_review INTEGER NOT NULL,
                               created_at DATETIME(6),
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               store_id BIGINT,
                               updated_at DATETIME(6),
                               content VARCHAR(1000) NOT NULL,
                               summary_type VARCHAR(255),
                               PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE image_tb (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          review_id BIGINT,
                          url VARCHAR(255) NOT NULL,
                          PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE likereview_tb (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               review_id BIGINT,
                               user_id BIGINT,
                               PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE likestore_tb (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              store_id BIGINT,
                              user_id BIGINT,
                              PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE refresh_token (
                               refresh_key VARCHAR(255) NOT NULL,
                               refresh_value VARCHAR(255),
                               PRIMARY KEY (refresh_key)
) ENGINE=InnoDB;

CREATE TABLE review_tb (
                           cost_per_person INTEGER,
                           people_count INTEGER,
                           rating INTEGER,
                           recommend_count INTEGER,
                           total_price INTEGER,
                           created_at DATETIME(6) NOT NULL,
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           store_id BIGINT,
                           updated_at DATETIME(6) NOT NULL,
                           user_id BIGINT NOT NULL,
                           content VARCHAR(1000) NOT NULL,
                           created_by VARCHAR(255),
                           review_uuid VARCHAR(255) NOT NULL UNIQUE,
                           updated_by VARCHAR(255),
                           PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE store_tb (
                          avg_cost_per_person INTEGER,
                          avg_rating FLOAT(53) NOT NULL,
                          avg_visit_count FLOAT(53),
                          latitude FLOAT(53) NOT NULL,
                          longitude FLOAT(53) NOT NULL,
                          nums_of_review INTEGER NOT NULL,
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          sub_category_id BIGINT,
                          address VARCHAR(255) NOT NULL,
                          business_hours VARCHAR(255) NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          phone_number VARCHAR(255) NOT NULL,
                          store_image_url VARCHAR(255),
                          PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE sub_category_tb (
                                 category_id BIGINT,
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 name VARCHAR(255),
                                 PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE tag_tb (
                        locationx FLOAT(53),
                        locationy FLOAT(53),
                        menu_rating INTEGER,
                        food_id BIGINT,
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        image_id BIGINT,
                        tag_name VARCHAR(255),
                        PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user_tb (
                         age_group TINYINT CHECK (age_group BETWEEN 0 AND 6),
                         email_verified BIT,
                         is_first_login BIT NOT NULL,
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         email VARCHAR(255) NOT NULL,
                         gender ENUM('FEMALE','MALE','UNKNOWN'),
                         locale VARCHAR(255),
                         name VARCHAR(255) NOT NULL,
                         profile_image_url VARCHAR(255),
                         provider ENUM('GOOGLE','KAKAO') NOT NULL,
                         provider_id VARCHAR(255),
                         PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE coin_earning_history_tb
    ADD CONSTRAINT FK_coin_earning_history_tb_coin_id
        FOREIGN KEY (coin_id) REFERENCES coin_tb (id);

ALTER TABLE coin_tb
    ADD CONSTRAINT FK_coin_tb_user_id
        FOREIGN KEY (user_id) REFERENCES user_tb (id);

ALTER TABLE coin_usage_history_tb
    ADD CONSTRAINT FK_coin_usage_history_tb_coin_id
        FOREIGN KEY (coin_id) REFERENCES coin_tb (id);

ALTER TABLE food_tb
    ADD CONSTRAINT FK_food_tb_store_id
        FOREIGN KEY (store_id) REFERENCES store_tb (id);

ALTER TABLE gpt_guidance_tb
    ADD CONSTRAINT FK_gpt_guidance_tb_user_id
        FOREIGN KEY (user_id) REFERENCES user_tb (id);

ALTER TABLE gpt_review_tb
    ADD CONSTRAINT FK_gpt_review_tb_store_id
        FOREIGN KEY (store_id) REFERENCES store_tb (id);

ALTER TABLE image_tb
    ADD CONSTRAINT FK_image_tb_review_id
        FOREIGN KEY (review_id) REFERENCES review_tb (id);

ALTER TABLE likereview_tb
    ADD CONSTRAINT FK_likereview_tb_review_id
        FOREIGN KEY (review_id) REFERENCES review_tb (id);

ALTER TABLE likereview_tb
    ADD CONSTRAINT FK_likereview_tb_user_id
        FOREIGN KEY (user_id) REFERENCES user_tb (id);

ALTER TABLE likestore_tb
    ADD CONSTRAINT FK_likestore_tb_store_id
        FOREIGN KEY (store_id) REFERENCES store_tb (id);

ALTER TABLE likestore_tb
    ADD CONSTRAINT FK_likestore_tb_user_id
        FOREIGN KEY (user_id) REFERENCES user_tb (id);

ALTER TABLE review_tb
    ADD CONSTRAINT FK_review_tb_store_id
        FOREIGN KEY (store_id) REFERENCES store_tb (id);

ALTER TABLE store_tb
    ADD CONSTRAINT FK_store_tb_sub_category_id
        FOREIGN KEY (sub_category_id) REFERENCES sub_category_tb (id);

ALTER TABLE sub_category_tb
    ADD CONSTRAINT FK_sub_category_tb_category_id
        FOREIGN KEY (category_id) REFERENCES category_tb (id);

ALTER TABLE tag_tb
    ADD CONSTRAINT FK_tag_tb_food_id
        FOREIGN KEY (food_id) REFERENCES food_tb (id);

ALTER TABLE tag_tb
    ADD CONSTRAINT FK_tag_tb_image_id
        FOREIGN KEY (image_id) REFERENCES image_tb (id);

CREATE INDEX IDX_sub_category_tb_category_id ON sub_category_tb (category_id);
