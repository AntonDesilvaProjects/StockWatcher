CREATE SCHEMA stock;

CREATE TABLE stock.portfolio (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(50),
    created_on TIMESTAMP,
    updated_on TIMESTAMP
);