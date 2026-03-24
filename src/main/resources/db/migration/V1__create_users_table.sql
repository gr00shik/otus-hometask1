CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    second_name VARCHAR(255) NOT NULL,
    birthdate DATE,
    biography TEXT,
    city VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
