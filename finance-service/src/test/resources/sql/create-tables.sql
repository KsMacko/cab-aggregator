-- card
CREATE TABLE IF NOT EXISTS card (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    last_four_digits VARCHAR(4) NOT NULL,
    expiration_date DATE NOT NULL,
    owner ENUM('DRIVER', 'PASSENGER') NOT NULL,
    owner_id BIGINT NOT NULL,
    card_type ENUM('VISA', 'MASTERCARD') NOT NULL
    );

-- driver_wallets
CREATE TABLE IF NOT EXISTS driver_wallets (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    driver_id BIGINT NOT NULL UNIQUE
    );

-- finance_operation
CREATE TABLE IF NOT EXISTS finance_operation (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 amount DECIMAL(19,2) NOT NULL,
    card_id BIGINT NULL,
    FOREIGN KEY (card_id) REFERENCES card(id)
    );

-- payment
CREATE TABLE IF NOT EXISTS payment (
                                       financial_operation_id BIGINT PRIMARY KEY,
                                       passenger_id BIGINT NOT NULL,
                                       payment_type VARCHAR(4) NOT NULL,
    FOREIGN KEY (financial_operation_id) REFERENCES finance_operation(id)
    );

-- wallet_transfer
CREATE TABLE IF NOT EXISTS wallet_transfer (
                                               financial_operation_id BIGINT PRIMARY KEY,
                                               remaining_amount DECIMAL(19,2) NOT NULL,
    driver_wallet_id BIGINT NOT NULL,
    FOREIGN KEY (financial_operation_id) REFERENCES finance_operation(id),
    FOREIGN KEY (driver_wallet_id) REFERENCES driver_wallets(id)
    );