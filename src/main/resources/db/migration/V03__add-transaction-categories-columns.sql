ALTER TABLE t_transaction_categories ADD COLUMN IF NOT EXISTS date_updated TIMESTAMP DEFAULT TO_TIMESTAMP(0);
ALTER TABLE t_transaction_categories ADD COLUMN IF NOT EXISTS date_added TIMESTAMP DEFAULT TO_TIMESTAMP(0);