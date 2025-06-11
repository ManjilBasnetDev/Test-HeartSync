-- Drop the database if it exists
DROP DATABASE IF EXISTS heartsync;

-- Create the database
CREATE DATABASE heartsync;

-- Use the database
USE heartsync;

-- Create users table if it doesn't exist (needed for foreign key constraints)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Create messages table
CREATE TABLE messages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_text TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);

-- Insert test users for development
INSERT INTO users (id, username, password_hash) VALUES 
(1, 'user1', 'test123'),
(2, 'user2', 'test123'); 