DROP TABLE IF EXISTS rate;
DROP TABLE IF EXISTS profile;

CREATE TABLE profile (
                         profile_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         first_name VARCHAR(20) NOT NULL,
                         email VARCHAR(50) NOT NULL UNIQUE,
                         phone VARCHAR(20) NOT NULL UNIQUE,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE rate (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      value INT NOT NULL CHECK (value BETWEEN 1 AND 5),
                      author_id BIGINT NOT NULL,
                      ride_id VARCHAR(20) NOT NULL,
                      passenger_profile_id BIGINT NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (passenger_profile_id) REFERENCES profile(profile_id)
);