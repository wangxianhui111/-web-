-- 部门数据初始化
INSERT INTO sys_dept (id, parent_id, name, leader, sort, status) VALUES 
(1, 0, '总公司', '管理员', 1, 1),
(2, 1, '研发部', '研发经理', 1, 1),
(3, 1, '市场部', '市场经理', 2, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 岗位数据初始化
INSERT INTO sys_post (id, code, name, description, sort, status) VALUES 
(1, 'CEO', '首席执行官', '公司最高管理者', 1, 1),
(2, 'CTO', '首席技术官', '技术负责人', 2, 1),
(3, 'DEV', '开发工程师', '技术开发岗', 3, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);
