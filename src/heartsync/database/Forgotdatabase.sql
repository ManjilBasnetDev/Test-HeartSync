-- Create the database
CREATE DATABASE IF NOT EXISTS heartsync_db;
USE heartsync_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    favorite_color VARCHAR(50) NOT NULL,
    first_school VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert some test data
INSERT INTO users (username, password, favorite_color, first_school) VALUES
('testuser', 'Test@123', 'blue', 'Springfield Elementary'),
('john_doe', 'John@123', 'red', 'Lincoln High'); 