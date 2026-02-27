-- 菜单管理模块迁移

-- 更新菜单，添加菜单管理菜单
INSERT INTO sys_menu (id, parent_id, name, path, component, icon, sort, visible, permission, menu_type, deleted, create_time, update_time)
VALUES 
(4, 1, '菜单管理', '/system/menu', 'system/menu/index', 'Menu', 3, 1, 'menu:list', 2, 0, NOW(), NOW()),
(13, 4, '新增菜单', NULL, NULL, NULL, 1, 0, 'menu:create', 3, 0, NOW(), NOW()),
(14, 4, '编辑菜单', NULL, NULL, NULL, 2, 0, 'menu:update', 3, 0, NOW(), NOW()),
(15, 4, '删除菜单', NULL, NULL, NULL, 3, 0, 'menu:delete', 3, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 更新角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id, create_time)
SELECT 1, id, NOW() FROM sys_menu WHERE id IN (4, 13, 14, 15)
ON DUPLICATE KEY UPDATE role_id = role_id;
