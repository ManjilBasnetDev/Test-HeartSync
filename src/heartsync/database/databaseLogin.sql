-- Create the database
CREATE DATABASE IF NOT EXISTS login_db;
USE login_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    security_question VARCHAR(255),
    security_answer VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a test user (password: password123)
INSERT INTO users (username, password, email, security_question, security_answer)
VALUES ('admin', 'password123', 'admin@example.com', 'What is your favorite color?', 'blue'); 
