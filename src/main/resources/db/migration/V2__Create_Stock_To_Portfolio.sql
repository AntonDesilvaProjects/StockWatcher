CREATE TABLE stock.stocktoportfolio (
    symbol VARCHAR(10) NOT NULL,
    portfolio_id INT NOT NULL,
    shares INT DEFAULT 0,
    comment VARCHAR(50),
    created_on TIMESTAMP,
    updated_on TIMESTAMP,
    PRIMARY KEY(symbol, portfolio_id)
);