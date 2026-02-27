-- 更新admin密码为admin123
UPDATE sys_user 
SET password = '$2a$10$6FtyVuB.3qdvw.NgXk9tguABnXKsd/J0OYnAzxy1ex0RqwVpfCbda' 
WHERE username = 'admin';
