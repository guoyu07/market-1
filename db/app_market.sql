/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50635
 Source Host           : localhost
 Source Database       : app_market

 Target Server Type    : MySQL
 Target Server Version : 50635
 File Encoding         : utf-8

 Date: 05/12/2017 11:56:56 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `tb_app_store`
-- ----------------------------
DROP TABLE IF EXISTS `tb_app_store`;
CREATE TABLE `tb_app_store` (
  `id` int(30) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_name` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `down_count` int(10) NOT NULL DEFAULT '0' COMMENT '下载次数',
  `app_location` varchar(100) DEFAULT NULL COMMENT '应用在服务器的路径',
  `icon_location` varchar(100) DEFAULT NULL COMMENT '图标在服务器的路径',
  `description` varchar(100) DEFAULT NULL COMMENT '应用描述',
  `app_version_name` varchar(30) DEFAULT NULL,
  `app_version_code` varchar(20) DEFAULT NULL,
  `file_size` varchar(30) DEFAULT NULL COMMENT '文件大小',
  `package_name` varchar(50) DEFAULT NULL,
  `order_by` int(5) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uid` int(11) NOT NULL COMMENT '创建人',
  `create_by` varchar(10) DEFAULT NULL,
  `update_by` varchar(10) DEFAULT NULL,
  `create_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_c_t` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `tb_imei_app`
-- ----------------------------
DROP TABLE IF EXISTS `tb_imei_app`;
CREATE TABLE `tb_imei_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imei_no` varchar(20) DEFAULT NULL COMMENT '终端号',
  `uid` int(11) DEFAULT NULL COMMENT '机构号',
  `app_id` int(11) DEFAULT NULL COMMENT '应用号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_i_a` (`imei_no`,`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `tb_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu` (
  `id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `short_en_name` varchar(50) NOT NULL,
  `icon` varchar(200) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `orderby` int(11) DEFAULT NULL,
  `permission_list` varchar(200) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `tb_menu`
-- ----------------------------
BEGIN;
INSERT INTO `tb_menu` VALUES ('1', '0', '系统管理', 'sysManage', '', '', '0', '', '1', '2017-02-10 16:51:05', '2017-02-10 16:51:05'), ('2', '0', '终端管理', 'terminalManage', '', '', '1', '', '1', '2017-02-10 16:51:05', '2017-04-17 16:05:50'), ('3', '0', '应用管理', 'appManage', '', '', '2', '', '1', '2017-02-10 16:51:05', '2017-04-17 16:34:06'), ('101', '1', '机构管理', 'userManage', '', '/user', '0', 'READ;CREATE;UPDATE;DELETE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-19 14:12:12'), ('102', '1', '角色管理', 'roleManage', '', '/role', '1', 'READ;CREATE;UPDATE;DELETE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-19 14:12:51'), ('103', '1', '日志查询', 'sysLogQry', '', '/sys/log', '2', 'READ', '1', '2017-02-10 16:51:05', '2017-04-17 16:13:50'), ('201', '2', '终端操作', 'terminalControl', '', '/terminal', '0', 'READ;CREATE;UPDATE;DELETE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-19 14:12:16'), ('202', '2', '位置查询', 'terminalPosition', '', '/terminal/position', '1', 'READ;CREATE;UPDATE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-17 16:12:20'), ('203', '2', '厂商应用', 'terminalSet', '', '/terminal/set', '3', 'READ;CREATE;UPDATE;EXPORT', '1', '2017-02-10 16:51:05', '2017-05-08 14:09:35'), ('301', '3', '应用操作', 'appControl', '', '/app', '0', 'READ;CREATE;UPDATE;DELETE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-19 14:12:28'), ('302', '3', '白名单', 'appWhitelist', '', '/app/whitelist', '1', 'READ;CREATE;UPDATE;DELETE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-19 14:12:37'), ('303', '3', '应用配置', 'appConfig', '', '/app/config', '2', 'READ;CREATE;UPDATE;DELETE;EXPORT', '1', '2017-02-10 16:51:05', '2017-04-19 14:12:56');
COMMIT;

-- ----------------------------
--  Table structure for `tb_operate_log`
-- ----------------------------
DROP TABLE IF EXISTS `tb_operate_log`;
CREATE TABLE `tb_operate_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_account` varchar(50) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'CURRENT_TIMESTAMP',
  `title` varchar(1000) DEFAULT NULL,
  `log` varchar(1000) DEFAULT NULL,
  `ip` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_account`) USING BTREE,
  KEY `idx_time` (`time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `tb_role`
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `memo` varchar(100) DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  `sys_level` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `tb_role`
-- ----------------------------
BEGIN;
INSERT INTO `tb_role` VALUES ('1', '超级管理员', '超级管理员组', 'admin', '1', '2017-04-17 16:27:13', '2017-04-17 16:38:35'), ('14', '机构管理员', '机构管理员', 'admin', '1', '2017-05-10 17:58:36', '2017-05-10 17:58:36');
COMMIT;

-- ----------------------------
--  Table structure for `tb_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_menu`;
CREATE TABLE `tb_role_menu` (
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `permission_sign` varchar(200) NOT NULL,
  `permission` varchar(200) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `tb_role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `tb_role_menu` VALUES ('1', '102', 'sysManage:roleManage:', 'READ;CREATE;UPDATE;DELETE;EXPORT', '2017-04-17 16:27:13', '2017-04-19 14:15:25'), ('1', '103', 'sysManage:sysLogQry:', 'READ;', '2017-04-17 16:27:13', '2017-04-19 14:17:49'), ('1', '101', 'sysManage:userManage:', 'READ;CREATE;UPDATE;DELETE;EXPORT', '2017-04-17 16:27:13', '2017-04-19 14:17:45'), ('1', '201', 'terminalManage:terminalControl:', 'READ;CREATE;UPDATE;DELETE;EXPORT', '2017-04-17 16:27:13', '2017-04-19 14:16:24'), ('1', '202', 'terminalManage:terminalPosition:', 'READ;', '2017-04-17 16:27:13', '2017-04-21 10:07:12'), ('1', '301', 'appManage:appControl:', 'READ;CREATE;UPDATE;DELETE;EXPORT', '2017-04-17 16:27:13', '2017-04-19 14:16:39'), ('1', '302', 'appManage:appWhitelist:', 'READ;CREATE;UPDATE;EXPORT;', '2017-04-17 16:27:13', '2017-04-19 12:23:13'), ('1', '303', 'appManage:appConfig:', 'READ;CREATE;UPDATE;DELETE;EXPORT', '2017-04-17 16:27:13', '2017-04-19 14:16:58'), ('1', '203', 'terminalManage:terminalSet:', 'READ;CREATE;UPDATE;DELETE;', '2017-04-17 16:27:13', '2017-04-20 15:59:53'), ('14', '201', 'terminalManage:terminalControl:', 'EXPORT;DELETE;UPDATE;CREATE;READ;', '2017-05-10 17:58:36', '2017-05-10 17:58:36'), ('14', '203', 'terminalManage:terminalSet:', 'DELETE;UPDATE;CREATE;READ;', '2017-05-10 17:58:36', '2017-05-10 17:58:36'), ('14', '202', 'terminalManage:terminalPosition:', 'READ;', '2017-05-10 17:58:36', '2017-05-10 17:58:36'), ('14', '301', 'appManage:appControl:', 'EXPORT;DELETE;UPDATE;CREATE;READ;', '2017-05-10 17:58:36', '2017-05-10 17:58:36'), ('14', '302', 'appManage:appWhitelist:', 'EXPORT;UPDATE;CREATE;READ;', '2017-05-10 17:58:36', '2017-05-10 17:58:36'), ('14', '303', 'appManage:appConfig:', 'EXPORT;DELETE;UPDATE;CREATE;READ;', '2017-05-10 17:58:36', '2017-05-10 17:58:36');
COMMIT;

-- ----------------------------
--  Table structure for `tb_sys_app_store`
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_app_store`;
CREATE TABLE `tb_sys_app_store` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `packet_name` varchar(50) DEFAULT NULL,
  `add_time` varchar(255) DEFAULT NULL,
  `descript` varchar(50) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `uid` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `idx_uid` (`uid`) USING BTREE,
  KEY `idx_c_t` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for `tb_terminal`
-- ----------------------------
DROP TABLE IF EXISTS `tb_terminal`;
CREATE TABLE `tb_terminal` (
  `imei` varchar(30) NOT NULL,
  `groupid` varchar(30) DEFAULT NULL COMMENT '组ID',
  `uid` int(11) DEFAULT NULL COMMENT '机构号',
  `shop_name` varchar(30) DEFAULT NULL COMMENT '店铺名称',
  `shop_phone` varchar(20) DEFAULT NULL COMMENT '店铺联系方式',
  `shop_contacts` varchar(30) DEFAULT NULL COMMENT '店铺联系人',
  `shop_longitude` decimal(10,6) NOT NULL DEFAULT '114.023459' COMMENT '店铺经度',
  `shop_latitude` decimal(10,6) NOT NULL DEFAULT '22.547910' COMMENT '店铺纬度',
  `sn` varchar(20) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`imei`),
  UNIQUE KEY `idx_sn` (`sn`) USING BTREE,
  UNIQUE KEY `idx_imei_sn` (`imei`,`sn`) USING BTREE,
  KEY `idx_c_t` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for `tb_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '机构id',
  `name` varchar(20) NOT NULL COMMENT '机构登陆名称',
  `password` varchar(32) NOT NULL COMMENT '机构登陆密码',
  `status` varchar(5) NOT NULL DEFAULT 'ON' COMMENT '可用状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` int(11) NOT NULL COMMENT '账户类型 1：管理员 2：普通用户',
  `account` varchar(20) NOT NULL COMMENT '机构名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
  KEY `idx_c_t` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `tb_user`
-- ----------------------------
BEGIN;
INSERT INTO `tb_user` VALUES ('1000000', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'ON', now(), now(), '1', '超级管理员');
COMMIT;

-- ----------------------------
--  Table structure for `tb_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role` (
  `role_id` int(11) NOT NULL,
  `user_account` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Records of `tb_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `tb_user_role` VALUES ('1', 'admin');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
