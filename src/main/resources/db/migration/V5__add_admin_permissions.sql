-- 为admin用户添加完整的用户管理权限

-- 更新现有菜单，添加权限标识
UPDATE sys_menu SET permission = 'user:list' WHERE id = 2;

-- 插入用户管理相关按钮权限
INSERT INTO sys_menu (id, parent_id, name, path, component, icon, sort, visible, permission, menu_type, deleted, create_time, update_time)
VALUES 
(5, 2, '新增用户', NULL, NULL, NULL, 1, 0, 'user:create', 3, 0, NOW(), NOW()),
(6, 2, '编辑用户', NULL, NULL, NULL, 2, 0, 'user:update', 3, 0, NOW(), NOW()),
(7, 2, '删除用户', NULL, NULL, NULL, 3, 0, 'user:delete', 3, 0, NOW(), NOW()),
(8, 2, '查看用户', NULL, NULL, NULL, 4, 0, 'user:query', 3, 0, NOW(), NOW());

-- 更新角色菜单关联，添加按钮权限
INSERT INTO sys_role_menu (role_id, menu_id, create_time)
VALUES 
(1, 5, NOW()),
(1, 6, NOW()),
(1, 7, NOW()),
(1, 8, NOW());

-- 验证数据
SELECT u.username, r.name as role_name, m.name as menu_name, m.permission 
FROM sys_user u
JOIN sys_user_role ur ON u.id = ur.user_id
JOIN sys_role r ON ur.role_id = r.id
JOIN sys_role_menu rm ON r.id = rm.role_id
JOIN sys_menu m ON rm.menu_id = m.id
WHERE u.username = 'admin';

