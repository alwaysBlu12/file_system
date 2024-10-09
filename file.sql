-- 创建数据库
create datebase file_system;

use file_system;

-- 用户表
CREATE TABLE user (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,//为了测试,这里是不唯一的
                       password VARCHAR(255) NOT NULL,
                       avatar_url VARCHAR(255),
                        is_admin INT,
                       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 文件表
-- 包括文件ID、文件名、文件类型、上传者ID、文件大小、上传时间、文件路径等。
CREATE TABLE file (
                       file_id INT AUTO_INCREMENT PRIMARY KEY,
                       file_name VARCHAR(255) NOT NULL,
                       file_type VARCHAR(50) NOT NULL,
                       user_id INT NOT NULL,
                       file_size BIGINT NOT NULL,
                       upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       file_path VARCHAR(255) NOT NULL,
                        is_delete int ,
                       FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 用户活动表
-- 记录用户的活动信息，包括活动ID、用户ID、活动类型、文件ID、活动时间、活动结果等。
CREATE TABLE user_activities (
                                 activity_id INT AUTO_INCREMENT PRIMARY KEY,
                                 user_id INT NOT NULL,
                                 activity_type VARCHAR(50) NOT NULL,
                                 file_id INT,
                                 activity_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 activity_result VARCHAR(50),
                                 FOREIGN KEY (user_id) REFERENCES user(user_id),
                                 FOREIGN KEY (file_id) REFERENCES file(file_id)
);

-- 用户空间表
CREATE TABLE user_file_space (
                                   space_id INT PRIMARY KEY,
                                    name varchar(50),
                                    description text,
                                   user_id INT NOT NULL,
                                   file_count INT,
                                   used_space BIGINT,
                                   total_space BIGINT,
                                    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);

-- 文件使用频率表
CREATE TABLE file_usage_frequency (
                                      file_id INT PRIMARY KEY,
                                      file_name VARCHAR(255),
                                      uploader_id INT,
                                      uploader_name VARCHAR(50),
                                      upload_time TIMESTAMP,
                                      download_count INT,
                                      last_download_time TIMESTAMP
);

-- 文件类型表
CREATE TABLE file_type_summary (
                                   file_type VARCHAR(50) PRIMARY KEY,
                                   file_count INT,
                                   total_storage BIGINT
);

-- 容量表-->这里可以是动态图显示
CREATE TABLE system_capacity_usage (
                                       total_capacity_gb INT PRIMARY KEY,
                                       used_capacity_gb FLOAT,
                                       remaining_capacity_gb FLOAT
);

