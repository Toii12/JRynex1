-- Personal Vault 数据库初始化脚本
-- 执行方式: mysql -u root -p < init_personal_vault.sql

CREATE DATABASE IF NOT EXISTS personal_vault
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE personal_vault;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 密码分类表
CREATE TABLE IF NOT EXISTS `password_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否预设分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='密码分类表';

-- 密码条目表
CREATE TABLE IF NOT EXISTS `password_entry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL COMMENT '所属用户',
  `category_id` INT DEFAULT NULL COMMENT '分类ID',
  `name` VARCHAR(100) NOT NULL COMMENT '名称（网站/APP名）',
  `url` VARCHAR(500) DEFAULT NULL COMMENT '网址',
  `username` VARCHAR(100) DEFAULT NULL COMMENT '用户名',
  `password` VARCHAR(200) DEFAULT NULL COMMENT '密码',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`category_id`) REFERENCES `password_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='密码条目表';

-- 财务分类表
CREATE TABLE IF NOT EXISTS `finance_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `type` VARCHAR(10) NOT NULL COMMENT 'income:收入 / expense:支出',
  `is_default` TINYINT(1) DEFAULT 0 COMMENT '是否预设分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务分类表';

-- 财务记录表
CREATE TABLE IF NOT EXISTS `finance_record` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL COMMENT '所属用户',
  `category_id` INT DEFAULT NULL COMMENT '分类ID',
  `type` VARCHAR(10) NOT NULL COMMENT 'income:收入 / expense:支出',
  `amount` DECIMAL(12,2) NOT NULL COMMENT '金额',
  `date` DATE NOT NULL COMMENT '日期',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`category_id`) REFERENCES `finance_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务记录表';

-- ============ 初始数据 ============

-- 预置用户
INSERT INTO `user` (`username`, `password`) VALUES
('admin', 'admin123');

-- 预设密码分类
INSERT INTO `password_category` (`name`, `is_default`) VALUES
('社交', 1),
('工作', 1),
('金融', 1),
('娱乐', 1),
('购物', 1),
('教育', 1),
('其他', 1);

-- 预设支出分类
INSERT INTO `finance_category` (`name`, `type`, `is_default`) VALUES
('餐饮', 'expense', 1),
('交通', 'expense', 1),
('购物', 'expense', 1),
('住房', 'expense', 1),
('医疗', 'expense', 1),
('娱乐', 'expense', 1),
('教育', 'expense', 1),
('其他', 'expense', 1);

-- 预设收入分类
INSERT INTO `finance_category` (`name`, `type`, `is_default`) VALUES
('工资', 'income', 1),
('兼职', 'income', 1),
('理财', 'income', 1),
('红包', 'income', 1),
('其他', 'income', 1);
