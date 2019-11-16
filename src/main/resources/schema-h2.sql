CREATE SEQUENCE t_category_category_id_seq start with 1001;

CREATE TABLE t_category(
  category_id INTEGER DEFAULT nextval('t_category_category_id_seq') NOT NULL,
  category VARCHAR(50)
);

CREATE TABLE t_transaction_categories(
  category_id INTEGER,
  transaction_id INTEGER
);

ALTER TABLE t_transaction_categories ADD CONSTRAINT transaction_categories_constraint UNIQUE(category_id, transaction_id);

CREATE SEQUENCE t_account_account_id_seq START WITH 1001;

CREATE TABLE t_account(
  account_id INTEGER DEFAULT nextval('t_account_account_id_seq') NOT NULL,
  account_name_owner CHAR(40) NOT NULL,
  account_name CHAR(20),
  account_owner CHAR(20),
  account_type CHAR(6) NOT NULL,
  active_status CHAR(1) NOT NULL,
  moniker CHAR(4),
  totals DECIMAL(12,2),
  totals_balanced DECIMAL(12,2),
  date_closed TIMESTAMP,
  date_updated TIMESTAMP,
  date_added TIMESTAMP
);

CREATE UNIQUE INDEX account_name_owner_idx on t_account(account_name_owner);

CREATE SEQUENCE t_transaction_transaction_id_seq start with 1001;

CREATE TABLE t_transaction (
  account_id INTEGER,
  account_type CHAR(6),
  account_name_owner CHAR(40),
  transaction_id INTEGER DEFAULT nextval('t_transaction_transaction_id_seq') NOT NULL,
  guid CHAR(36) NOT NULL,
  sha256 CHAR(70),
  transaction_date DATE,
  description VARCHAR(75),
  category VARCHAR(50),
  amount DECIMAL(12,2),
  cleared INTEGER,
  reoccurring BOOLEAN,
  notes VARCHAR(100),
  date_updated TIMESTAMP,
  date_added TIMESTAMP
);

ALTER TABLE t_transaction ADD CONSTRAINT transaction_constraint UNIQUE (account_name_owner, transaction_date, description, category, amount, notes);

CREATE UNIQUE INDEX guid_idx ON t_transaction(guid);

CREATE TABLE t_test2 (
  test_id INTEGER
)

--insert t_test(test_id) VALUES(1)
