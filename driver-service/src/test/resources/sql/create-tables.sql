CREATE TABLE IF NOT EXISTS profile (
                                       profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    fare_type ENUM('ECONOMY', 'BUSINESS', 'COMFORT') NOT NULL,
    driver_status ENUM('FREE', 'DRIVING_TO_CLIENT', 'WAITING_FOR_CLIENT', 'IN_TRANSIT', 'ON_BREAK') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS car (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   car_number VARCHAR(20) NOT NULL UNIQUE,
    brand VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    driver_profile_profile_id BIGINT,
    FOREIGN KEY (driver_profile_profile_id) REFERENCES profile(profile_id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS notification (
                                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            ride_id VARCHAR(255),
    type ENUM('CASH_CONFIRMATION', 'RIDE_CREATION') NOT NULL,
    status ENUM('ACCEPTED', 'REJECTED', 'NON_VIEWED') NOT NULL,
    activity ENUM('ACTIVE', 'NON_ACTIVE') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    driver_profile_id BIGINT,
    FOREIGN KEY (driver_profile_id) REFERENCES profile(profile_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS cash_confirmation (
                                                 id BIGINT PRIMARY KEY,
                                                 amount DECIMAL(10, 2) NOT NULL,
    passenger_id BIGINT NOT NULL,
    FOREIGN KEY (id) REFERENCES notification(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS ride_creation (
                                             id BIGINT PRIMARY KEY,
                                             start_location VARCHAR(255),
    end_locations VARCHAR(255),
    FOREIGN KEY (id) REFERENCES notification(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS rate (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    value INT NOT NULL CHECK (value BETWEEN 1 AND 5),
    author_id BIGINT NOT NULL,
    ride_id VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    driver_profile_profile_id BIGINT,
    FOREIGN KEY (driver_profile_profile_id) REFERENCES profile(profile_id) ON DELETE SET NULL
    );