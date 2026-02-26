-- 更新管理员密码为admin123
UPDATE sys_user SET password = '$2a$10$r5nBPbcoy3nfLbPR2oMRruKxshATQgu9nUdFAvrI1dUTSKVzXO/3m' WHERE username = 'admin';
