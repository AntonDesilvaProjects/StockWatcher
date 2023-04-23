-- Add the status column to the Portfolio table
ALTER TABLE stock.portfolio ADD status INT NOT NULL DEFAULT 1;

-- Change the name of the 'shares' column to 'quantity'
ALTER TABLE stock.stocktoportfolio RENAME COLUMN shares TO quantity;