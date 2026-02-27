-- 岗位管理模块迁移

-- 创建岗位表
CREATE TABLE IF NOT EXISTS `sys_post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code` VARCHAR(50) NOT NULL COMMENT '岗位编码',
    `name` VARCHAR(50) NOT NULL COMMENT '岗位名称',
    `description` VARCHAR(200) COMMENT '岗位描述',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0=未删除,1=已删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统岗位表';

-- 初始化岗位数据
INSERT INTO sys_post (id, code, name, description, sort, status, deleted)
VALUES 
(1, 'CEO', '首席执行官', '公司最高管理者', 1, 1, 0),
(2, 'CTO', '首席技术官', '公司技术负责人', 2, 1, 0),
(3, 'CFO', '首席财务官', '公司财务负责人', 3, 1, 0),
(4, 'MGR', '部门经理', '部门管理岗', 4, 1, 0),
(5, 'DEV', '开发工程师', '技术开发岗', 5, 1, 0),
(6, 'TEST', '测试工程师', '质量测试岗', 6, 1, 0),
(7, 'HR', '人事专员', '人力资源岗', 7, 1, 0)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 添加岗位管理菜单
INSERT INTO sys_menu (id, parent_id, name, path, component, icon, sort, visible, permission, menu_type, deleted, create_time, update_time)
VALUES 
(30, 1, '岗位管理', '/system/post', 'system/post/index', 'Post', 5, 1, 'post:list', 2, 0, NOW(), NOW()),
(31, 30, '新增岗位', NULL, NULL, NULL, 1, 0, 'post:create', 3, 0, NOW(), NOW()),
(32, 30, '编辑岗位', NULL, NULL, NULL, 2, 0, 'post:update', 3, 0, NOW(), NOW()),
(33, 30, '删除岗位', NULL, NULL, NULL, 3, 0, 'post:delete', 3, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 更新角色菜单关联
INSERT INTO sys_role_menu (role_id, menu_id, create_time)
SELECT 1, id, NOW() FROM sys_menu WHERE id IN (30, 31, 32, 33)
ON DUPLICATE KEY UPDATE role_id = role_id;
