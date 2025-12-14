-- 1) Create database
CREATE DATABASE IF NOT EXISTS `taskdb`;

USE `taskdb`;

-- 2) Create application user and grant privileges
CREATE USER IF NOT EXISTS 'taskuser'@'%' IDENTIFIED BY 'taskpass';
GRANT ALL PRIVILEGES ON `taskdb`.* TO 'taskuser'@'%';
FLUSH PRIVILEGES;

-- 3) Users table
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `firstname` VARCHAR(100) NOT NULL,
  `lastname` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_users_username` (`username`),
  UNIQUE KEY `ux_users_email` (`email`)
);

-- 4) Roles table as element collection (many simple roles per user)
CREATE TABLE `user_roles` (
  `user_id` BIGINT NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`user_id`, `role`),
  CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 5) Tasks table
DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` TEXT,
  `status` VARCHAR(255) NOT NULL,
  `assigned_to` VARCHAR(255) NOT NULL,
  `completed` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `idx_tasks_created_at` (`created_at`),
  CONSTRAINT `fk_tasks_users` FOREIGN KEY (`assigned_to`)
    REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
);
