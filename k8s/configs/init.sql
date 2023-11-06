CREATE SCHEMA IF NOT EXISTS `matgpt_db` DEFAULT CHARACTER SET utf8mb4;

USE `matgpt_db`;

CREATE TABLE IF NOT EXISTS category_tb (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS food_tb (
    review_count INT,
    total_rating FLOAT,
    created_at TIMESTAMP NOT NULL,
    id BIGINT AUTO_INCREMENT,
    store_id BIGINT,
    updated_at TIMESTAMP NOT NULL,
    food_description VARCHAR(255),
    food_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS gpt_guidance_tb (
    created_at TIMESTAMP NOT NULL,
    id BIGINT AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS gpt_review_tb (
    last_nums_of_review INT NOT NULL,
    created_at TIMESTAMP,
    id BIGINT AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    updated_at TIMESTAMP,
    content VARCHAR(1000) NOT NULL,
    summary_type VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS image_tb (
    id BIGINT AUTO_INCREMENT,
    review_id BIGINT,
    url VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS likestore_tb (
    id BIGINT AUTO_INCREMENT,
    store_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS review_tb (
    cost_per_person INT,
    people_count INT,
    rating FLOAT,
    recommend_count INT,
    total_price INT,
    created_at TIMESTAMP NOT NULL,
    id BIGINT AUTO_INCREMENT,
    store_id BIGINT,
    updated_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS store_tb (
    avg_cost_per_person FLOAT,
    avg_visit_count INT,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    nums_of_review INT NOT NULL,
    rating_avg FLOAT NOT NULL,
    id BIGINT AUTO_INCREMENT,
    sub_category_id BIGINT,
    address VARCHAR(255) NOT NULL,
    business_hours VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    store_img VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sub_category_tb (
    category_id BIGINT,
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tag_tb (
    locationx INT,
    locationy INT,
    menu_rating FLOAT,
    food_id BIGINT,
    id BIGINT AUTO_INCREMENT,
    image_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_tb (
    age_group TINYINT CHECK (age_group BETWEEN 0 AND 6),
    email_verified BOOLEAN,
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    gender VARCHAR(255) CHECK (gender IN ('MALE', 'FEMALE', 'UNKNOWN')),
    name VARCHAR(255) NOT NULL,
    provider VARCHAR(255) NOT NULL CHECK (provider IN ('GOOGLE', 'KAKAO')),
    provider_id VARCHAR(255),
    PRIMARY KEY (id)
);
