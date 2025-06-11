-- Create the database
CREATE DATABASE IF NOT EXISTS heartsync;
USE heartsync;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(10) DEFAULT 'USER',
    email VARCHAR(100),
    phone_number VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10),
    interests TEXT,
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a test user (password: test123)
INSERT INTO users (username, password, user_type, email)
VALUES ('testuser', 'test123', 'USER', 'test@example.com')
ON DUPLICATE KEY UPDATE password = VALUES(password);