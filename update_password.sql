-- 更新管理员密码
UPDATE scaffold.sys_user SET password = '$2a$10$JEC/3OCqKQehfUCYmaYJJ.1MqsC6yE.z5YqzWwLQ8lWz7p.fWqGVy' WHERE username = 'admin';

-- 验证更新结果
SELECT id, username, nickname, email, status FROM scaffold.sys_user;
