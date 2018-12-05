/*
SQLyog Community v12.3.2 (64 bit)
MySQL - 5.7.18 : Database - app_market
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`app_market` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `app_market`;

/*Table structure for table `tb_app_store` */

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
  `app_Type` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_c_t` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tb_app_type` */

DROP TABLE IF EXISTS `tb_app_type`;

CREATE TABLE `tb_app_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '类别名称',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_area` */

DROP TABLE IF EXISTS `tb_area`;

CREATE TABLE `tb_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

/*Table structure for table `tb_imei_app` */

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
) ENGINE=InnoDB AUTO_INCREMENT=1150 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tb_menu` */

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

/*Table structure for table `tb_operate_log` */

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
) ENGINE=InnoDB AUTO_INCREMENT=2015 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tb_role` */

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

/*Table structure for table `tb_role_menu` */

DROP TABLE IF EXISTS `tb_role_menu`;

CREATE TABLE `tb_role_menu` (
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `permission_sign` varchar(200) NOT NULL,
  `permission` varchar(200) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tb_sys_app_store` */

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

/*Table structure for table `tb_terminal` */

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
  `remark` varchar(1024) DEFAULT NULL COMMENT ' 备注',
  `area` varchar(128) DEFAULT NULL,
  `userIdName` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`imei`),
  UNIQUE KEY `idx_sn` (`sn`) USING BTREE,
  UNIQUE KEY `idx_imei_sn` (`imei`,`sn`) USING BTREE,
  KEY `idx_c_t` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tb_user` */

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
) ENGINE=InnoDB AUTO_INCREMENT=1000020 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tb_user_role` */

DROP TABLE IF EXISTS `tb_user_role`;

CREATE TABLE `tb_user_role` (
  `role_id` int(11) NOT NULL,
  `user_account` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
