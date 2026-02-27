-- 字典管理模块迁移

-- 创建字典类型表
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(100) NOT NULL COMMENT '字典名称',
    `code` VARCHAR(100) NOT NULL COMMENT '字典编码',
    `description` VARCHAR(200) COMMENT '字典描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0=未删除,1=已删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

-- 创建字典数据表
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dict_type_id` BIGINT NOT NULL COMMENT '字典类型ID',
    `label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `value` VARCHAR(100) NOT NULL COMMENT '字典值',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0=未删除,1=已删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_dict_type_id` (`dict_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

-- 初始化字典类型
INSERT INTO sys_dict_type (id, name, code, description, status, deleted)
VALUES 
(1, '用户状态', 'user_status', '用户状态列表', 1, 0),
(2, '菜单类型', 'menu_type', '菜单类型列表', 1, 0),
(3, '系统开关', 'system_switch', '系统开关配置', 1, 0)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 初始化字典数据
INSERT INTO sys_dict_data (dict_type_id, label, value, sort, status, deleted)
VALUES 
(1, '正常', '1', 1, 1, 0),
(1, '禁用', '0', 2, 1, 0),
(2, '目录', '1', 1, 1, 0),
(2, '菜单', '2', 2, 1, 0),
(2, '按钮', '3', 3, 1, 0),
(3, '开启', 'true', 1, 1, 0),
(3, '关闭', 'false', 2, 1, 0)
ON DUPLICATE KEY UPDATE label = VALUES(label);

-- 添加字典管理菜单
INSERT INTO sys_menu (id, parent_id, name, path, component, icon, sort, visible, permission, menu_type, deleted, create_time, update_time)
VALUES 
(40, 1, '字典管理', '/system/dict', 'system/dict/index', 'Dict', 6, 1, 'dict:list', 2, 0, NOW(), NOW()),
(41, 40, '新增字典', NULL, NULL, NULL, 1, 0, 'dict:create', 3, 0, NOW(), NOW()),
(42, 40, '编辑字典', NULL, NULL, NULL, 2, 0, 'dict:update', 3, 0, NOW(), NOW()),
(43, 40, '删除字典', NULL, NULL, NULL, 3, 0, 'dict:delete', 3, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 更新角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id, create_time)
SELECT 1, id, NOW() FROM sys_menu WHERE id IN (40, 41, 42, 43)
ON DUPLICATE KEY UPDATE role_id = role_id;
