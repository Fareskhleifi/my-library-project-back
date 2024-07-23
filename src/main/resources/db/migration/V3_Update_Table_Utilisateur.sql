ALTER TABLE utilisateurs
ADD COLUMN reset_token VARCHAR(255);

ALTER TABLE utilisateurs
ADD COLUMN reset_token_expiry TIMESTAMP;