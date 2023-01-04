-- password Admin000
INSERT INTO `rbac_user` (`id`, `account`, `enable`, `password`, `tel`, `user_name`) VALUES (NULL, 'admin', b'1', 'b0dc5be3a5ac529022294740086a4725', '110', '超级管理员');
-- permission
INSERT INTO `rbac_permission` (`id`, `description`, `enable`, `name`, `path`, `permission_key`, `resource`, `type`, `weight`, `parent_id`) VALUES (NULL, '系统管理', b'1', '系统管理', '', 'system', '', 'MENU', '0', NULL);
INSERT INTO `rbac_permission` (`id`, `description`, `enable`, `name`, `path`, `permission_key`, `resource`, `type`, `weight`, `parent_id`) VALUES (NULL, '权限管理', b'1', '权限管理', '/system/permission', 'system:permission', '', 'MENU', '1', '1');
INSERT INTO `rbac_permission` (`id`, `description`, `enable`, `name`, `path`, `permission_key`, `resource`, `type`, `weight`, `parent_id`) VALUES (NULL, '角色管理', b'1', '角色管理', '/system/role', 'system:role', '', 'MENU', '2', '1');
INSERT INTO `rbac_permission` (`id`, `description`, `enable`, `name`, `path`, `permission_key`, `resource`, `type`, `weight`, `parent_id`) VALUES (NULL, '用户管理', b'1', '用户管理', '/system/user', 'system:user', '', 'MENU', '3', '1');