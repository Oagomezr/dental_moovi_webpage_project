USE dental_moovi;
DROP DATABASE IF EXISTS prueba;
CREATE DATABASE IF NOT EXISTS prueba;
USE prueba;

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    id_parent_category BIGINT,
    FOREIGN KEY (id_parent_category) REFERENCES categories(id)
);

CREATE TABLE images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    content_type VARCHAR(30),
    data LONGBLOB,
    id_product BIGINT
);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(70) NOT NULL UNIQUE,
    description VARCHAR(70),
    unit_price DECIMAL(10,2),
    stock INT,
    open_to_public BOOLEAN,
    id_category BIGINT,
    id_main_image BIGINT,
    FOREIGN KEY (id_category) REFERENCES categories(id),
    FOREIGN KEY (id_main_image) REFERENCES images(id)
);

CREATE TABLE users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(70) NOT NULL UNIQUE,
    cel_phone VARCHAR(13) NOT NULL,
    birthdate DATE,
    gender VARCHAR(15),
    password VARCHAR(255)
);

CREATE TABLE roles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(15)
);

CREATE TABLE users_roles(
    id_user BIGINT,
    id_role BIGINT,
    PRIMARY KEY (id_user, id_role)--,
    --FOREIGN KEY (id_user) REFERENCES users(id),
    --FOREIGN KEY (id_role) REFERENCES roles(id)
);

ALTER TABLE images
ADD FOREIGN KEY (id_product) REFERENCES products(id);