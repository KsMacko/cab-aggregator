SELECT '✅=============== Script started: Creating databases...' AS status;

CREATE DATABASE IF NOT EXISTS driver_db;
CREATE DATABASE IF NOT EXISTS passenger_db;
CREATE DATABASE IF NOT EXISTS finance_db;

SELECT '✅================== Databases created: driver_db, passenger_db, finance_db' AS status;