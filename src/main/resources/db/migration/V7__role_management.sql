-- 角色管理模块迁移

-- 确保sys_role表有正确的字段（使用code作为唯一键，name作为显示名称）
UPDATE sys_role SET code = 'SUPER_ADMIN', name = '超级管理员' WHERE id = 1;
UPDATE sys_role SET code = 'NORMAL_USER', name = '普通用户' WHERE id = 2;

-- 更新菜单，添加角色管理菜单
INSERT INTO sys_menu (id, parent_id, name, path, component, icon, sort, visible, permission, menu_type, deleted, create_time, update_time)
VALUES 
(3, 1, '角色管理', '/system/role', 'system/role/index', 'Role', 2, 1, 'role:list', 2, 0, NOW(), NOW()),
(9, 3, '新增角色', NULL, NULL, NULL, 1, 0, 'role:create', 3, 0, NOW(), NOW()),
(10, 3, '编辑角色', NULL, NULL, NULL, 2, 0, 'role:update', 3, 0, NOW(), NOW()),
(11, 3, '删除角色', NULL, NULL, NULL, 3, 0, 'role:delete', 3, 0, NOW(), NOW()),
(12, 3, '查看角色', NULL, NULL, NULL, 4, 0, 'role:query', 3, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 更新角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id, create_time)
SELECT 1, id, NOW() FROM sys_menu WHERE id IN (3, 9, 10, 11, 12)
ON DUPLICATE KEY UPDATE role_id = role_id;
