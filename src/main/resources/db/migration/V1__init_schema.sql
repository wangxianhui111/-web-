# 系统用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(Bcrypt加密)',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0=未删除,1=已删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` VARCHAR(50) COMMENT '创建人',
    `update_by` VARCHAR(50) COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

# 系统角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(200) COMMENT '角色描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0=未删除,1=已删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

# 系统菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `path` VARCHAR(200) COMMENT '路由路径',
    `component` VARCHAR(255) COMMENT '组件路径',
    `icon` VARCHAR(50) COMMENT '菜单图标',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '显示状态(0=隐藏,1=显示)',
    `permission` VARCHAR(100) COMMENT '权限标识',
    `menu_type` TINYINT NOT NULL DEFAULT 1 COMMENT '菜单类型(1=目录,2=菜单,3=按钮)',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记(0=未删除,1=已删除)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';

# 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

# 角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

# 初始化超级管理员用户 (密码: 123456)
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `status`, `deleted`)
VALUES (1, 'admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '超级管理员', 'admin@company.com', 1, 0);

# 初始化角色
INSERT INTO `sys_role` (`id`, `code`, `name`, `description`, `status`)
VALUES (1, 'SUPER_ADMIN', '超级管理员', '拥有所有权限', 1),
       (2, 'NORMAL_USER', '普通用户', '普通用户权限', 1);

# 初始化菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `icon`, `sort`, `visible`, `menu_type`)
VALUES (1, 0, '系统管理', '/system', 'system/index', 'Setting', 1, 1, 1),
       (2, 1, '用户管理', '/system/user', 'system/user/index', 'User', 1, 1, 2),
       (3, 1, '角色管理', '/system/role', 'system/role/index', 'Role', 2, 1, 2),
       (4, 1, '菜单管理', '/system/menu', 'system/menu/index', 'Menu', 3, 1, 2);

# 初始化用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`)
VALUES (1, 1);

# 初始化角色菜单关联
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
VALUES (1, 1), (1, 2), (1, 3), (1, 4);
