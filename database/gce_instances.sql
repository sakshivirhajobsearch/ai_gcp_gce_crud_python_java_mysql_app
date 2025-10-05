CREATE DATABASE IF NOT EXISTS ai_gcp_gce;

USE ai_gcp_gce;

CREATE TABLE IF NOT EXISTS gce_instances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    zone VARCHAR(255),
    status VARCHAR(100),
    cpu_platform VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
