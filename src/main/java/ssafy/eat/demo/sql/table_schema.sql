CREATE DATABASE IF NOT EXISTS ssafy_eat;
USE ssafy_eat;

-- Rating Tables
CREATE TABLE naver_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating DOUBLE,
    rated_at TIMESTAMP
);

CREATE TABLE kakao_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating DOUBLE,
    rated_at TIMESTAMP
);

CREATE TABLE google_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating DOUBLE,
    rated_at TIMESTAMP
);

-- Restaurant Table
CREATE TABLE restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    description VARCHAR(1000),
    cuisine_type VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    naver_rating_id BIGINT,
    kakao_rating_id BIGINT,
    google_rating_id BIGINT,
    FOREIGN KEY (naver_rating_id) REFERENCES naver_ratings(id),
    FOREIGN KEY (kakao_rating_id) REFERENCES kakao_ratings(id),
    FOREIGN KEY (google_rating_id) REFERENCES google_ratings(id)
);

-- User Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Wishlist Junction Table (for Many-to-Many relationship)
CREATE TABLE user_wishlist (
    user_id BIGINT,
    restaurant_id BIGINT,
    PRIMARY KEY (user_id, restaurant_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);
