BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account CASCADE;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

DROP TABLE IF EXISTS transfer, transfer_status, transfer_type;
DROP SEQUENCE IF EXISTS seq_transfer_id;

CREATE TABLE transfer_status (
	status_id serial,
	transfer_status varchar(200),
	CONSTRAINT PK_transfer_status PRIMARY KEY (status_id)
);

CREATE TABLE transfer_type (
	type_id serial,
	transfer_type varchar(200),
	CONSTRAINT PK_transfer_type PRIMARY KEY (type_id)
);

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	sender_id int NOT NULL,
	recipient_id int NOT NULL,
	status_id int NOT NULL,
	type_id int NOT NULL,
	amount numeric(13, 2) NOT NULL,
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_transfer_sender_id FOREIGN KEY (sender_id) REFERENCES tenmo_user(user_id),
	CONSTRAINT FK_transfer_recipient_id FOREIGN KEY (recipient_id) REFERENCES tenmo_user(user_id),
	CONSTRAINT FK_transfer_status_id FOREIGN KEY (status_id) REFERENCES transfer_status(status_id),
	CONSTRAINT FK_transfer_type_id FOREIGN KEY (type_id) REFERENCES transfer_type(type_id)
);

INSERT INTO tenmo_user (username, password_hash)
VALUES ('bob', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
       ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy');

INSERT INTO account (user_id, balance)
VALUES (1001, 1000.00),
       (1002, 1000.00);

INSERT INTO transfer_status(transfer_status)
VALUES ('Approved'),
       ('Pending'),
       ('Rejected');

INSERT into transfer_type(transfer_type)
VALUES ('Send'),
       ('Request');

INSERT into transfer(sender_id, recipient_id, status_id, type_id, amount)
VALUES (1001, 1002, 1, 1, 300),
       (1001, 1002, 2, 2, 500);

COMMIT;