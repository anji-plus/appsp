/*
 Navicat Premium Data Transfer

 Source Server         : sp_open
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 127.0.0.1:3306
 Source Schema         : open_app_sp_service_platform

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 16/12/2020 15:43:54
*/
-- 创建数据库
CREATE DATABASE IF NOT EXISTS open_app_sp_service_platform DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
-- 使用数据库
USE open_app_sp_service_platform;

-- 开始创建对应表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sp_app_device
-- ----------------------------
DROP TABLE IF EXISTS `sp_app_device`;
CREATE TABLE `sp_app_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '应用唯一key',
  `device_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '设备唯一标识',
  `platform` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '平台(iOS/Android)',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='app设备唯一标识表';

-- ----------------------------
-- Table structure for sp_app_log
-- ----------------------------
DROP TABLE IF EXISTS `sp_app_log`;
CREATE TABLE `sp_app_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '应用唯一key',
  `device_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '设备唯一标识',
  `ip` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT 'ip地址',
  `regional` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '地域（中国|0|上海|上海市|电信）（0|0|0|内网IP|内网IP）',
  `net_work_status` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '联网方式',
  `platform` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '平台(iOS/Android)',
  `screen_info` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '高*宽',
  `brand` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '设备品牌（例如：小米6，iPhone 7plus）',
  `version_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '版本名',
  `version_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '版本号',
  `sdk_version` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT 'sdk版本',
  `os_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '系统版本例如：7.1.2  13.4.1',
  `interface_type` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '接口类型',
  `interface_type_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '接口类型名',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='app请求log';

-- ----------------------------
-- Table structure for sp_app_release
-- ----------------------------
DROP TABLE IF EXISTS `sp_app_release`;
CREATE TABLE `sp_app_release` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '应用唯一key',
  `device_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '设备唯一标识',
  `version_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '版本名',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='app灰度发布用户信息';

-- ----------------------------
-- Table structure for sp_application
-- ----------------------------
DROP TABLE IF EXISTS `sp_application`;
CREATE TABLE `sp_application` (
  `app_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(50) DEFAULT NULL COMMENT '应用唯一key',
  `name` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `public_key` varchar(2048) DEFAULT NULL COMMENT '公钥',
  `private_key` varchar(2048) DEFAULT NULL COMMENT '私钥',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `app_key_unique` (`app_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用';

-- ----------------------------
-- Table structure for sp_dict
-- ----------------------------
DROP TABLE IF EXISTS `sp_dict`;
CREATE TABLE `sp_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '标签名',
  `value` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '数据值',
  `type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '类型（Android版本、iOS版本、公告类型、展示类型）',
  `description` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '描述（Android版本、iOS版本、公告类型、展示类型）',
  `parent_id` bigint(2) DEFAULT '0' COMMENT '父级编号：0代表没有父级',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

-- ----------------------------
-- Records of sp_dict
-- ----------------------------
BEGIN;
INSERT INTO `sp_dict` VALUES (1, '6', 'Android6', 'ANDROID_VERSION', 'Android版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (2, '7', 'Android7', 'ANDROID_VERSION', 'Android版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (3, '8', 'Android8', 'ANDROID_VERSION', 'Android版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (4, '9', 'Android9', 'ANDROID_VERSION', 'Android版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (5, '10', 'Android10', 'ANDROID_VERSION', 'Android版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (6, '10', 'iOS10', 'IOS_VERSION', 'iOS版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-26 18:48:53', NULL);
INSERT INTO `sp_dict` VALUES (7, '11', 'iOS11', 'IOS_VERSION', 'iOS版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-26 18:40:44', NULL);
INSERT INTO `sp_dict` VALUES (8, '12', 'iOS12', 'IOS_VERSION', 'iOS版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (9, '13', 'iOS13', 'IOS_VERSION', 'iOS版本号', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (10, '14', 'iOS14', 'IOS_VERSION', 'iOS版本号', 0, 1, 0, 1, '2020-10-23 14:08:09', 1, '2020-10-23 14:08:09', NULL);
INSERT INTO `sp_dict` VALUES (11, 'horizontal_scroll', '水平滚动', 'TEMPLATE', '模板类型', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_dict` VALUES (12, 'dialog', '弹框', 'TEMPLATE', '模板类型', 0, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sp_menu
-- ----------------------------
DROP TABLE IF EXISTS `sp_menu`;
CREATE TABLE `sp_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(10) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(2) DEFAULT NULL COMMENT '排序',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单';

-- ----------------------------
-- Records of sp_menu
-- ----------------------------
BEGIN;
INSERT INTO `sp_menu` VALUES (1, 0, '账号管理', NULL, NULL, 0, NULL, 10, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_menu` VALUES (2, 1, '版本管理', NULL, 'system:user:version', 1, NULL, 100, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_menu` VALUES (3, 1, '公告管理', NULL, 'system:user:notice', 1, NULL, 101, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_menu` VALUES (4, 1, '成员管理', NULL, 'system:user:members', 1, NULL, 102, 1, 0, 1, '2020-06-19 12:24:50', 1, '2020-06-19 12:24:56', NULL);
INSERT INTO `sp_menu` VALUES (5, 0, '基础设置', NULL, NULL, 0, NULL, 11, 1, 0, 1, '2020-07-01 12:21:58', 1, '2020-07-01 12:22:04', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sp_notice
-- ----------------------------
DROP TABLE IF EXISTS `sp_notice`;
CREATE TABLE `sp_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公告名称',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '公告标题',
  `details` varchar(500) DEFAULT NULL COMMENT '公告内容',
  `template_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '模板类型（来自字典表）',
  `template_type_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '模板类型名',
  `result_type` int(10) DEFAULT NULL COMMENT '效果类型（来自字典表）',
  `result_type_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '效果类型名',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告管理';

-- ----------------------------
-- Table structure for sp_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sp_oper_log`;
CREATE TABLE `sp_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(20) CHARACTER SET utf8 DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8 DEFAULT '' COMMENT '请求方式',
  `oper_name` varchar(50) CHARACTER SET utf8 DEFAULT '' COMMENT '操作人员',
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用id',
  `oper_url` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) CHARACTER SET utf8 DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '操作地点',
  `oper_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '请求参数',
  `json_result` text CHARACTER SET utf8 COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8 DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `begin_time` bigint(20) DEFAULT NULL COMMENT '请求开始时间戳',
  `end_time` bigint(20) DEFAULT NULL COMMENT '请求结束时间戳',
  `time` bigint(20) DEFAULT NULL COMMENT '接口耗时-毫秒',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ----------------------------
-- Table structure for sp_role
-- ----------------------------
DROP TABLE IF EXISTS `sp_role`;
CREATE TABLE `sp_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `role_sign` varchar(20) DEFAULT NULL COMMENT '角色标识',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sp_role
-- ----------------------------
BEGIN;
INSERT INTO `sp_role` VALUES (1, '产品经理', '1', 1, 0, 1, '2020-06-19 12:11:59', 2, '2020-12-01 12:19:44', '产品角色');
INSERT INTO `sp_role` VALUES (2, '测试人员', '1', 1, 0, 1, '2020-06-19 12:11:59', 2, '2020-12-01 12:19:55', '测试角色');
INSERT INTO `sp_role` VALUES (3, '移动开发', '1', 1, 0, 1, '2020-06-19 12:11:59', 2, '2020-12-01 12:20:01', '移动开发角色');
COMMIT;

-- ----------------------------
-- Table structure for sp_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sp_role_menu`;
CREATE TABLE `sp_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sp_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sp_role_menu` VALUES (1, 1, 2);
INSERT INTO `sp_role_menu` VALUES (2, 1, 3);
INSERT INTO `sp_role_menu` VALUES (3, 1, 4);
INSERT INTO `sp_role_menu` VALUES (4, 2, 3);
INSERT INTO `sp_role_menu` VALUES (5, 2, 2);
INSERT INTO `sp_role_menu` VALUES (6, 3, 2);
COMMIT;

-- ----------------------------
-- Table structure for sp_user
-- ----------------------------
DROP TABLE IF EXISTS `sp_user`;
CREATE TABLE `sp_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '账号',
  `name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `is_admin` int(11) NOT NULL DEFAULT '0' COMMENT '0--不是admin 1--是admin',
  `sex` bigint(20) DEFAULT NULL COMMENT '性别',
  `pic_url` bigint(20) DEFAULT NULL,
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_unique` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sp_user
-- ----------------------------
BEGIN;
INSERT INTO `sp_user` VALUES (1, 'admin', '管理员', '$2a$10$nbGK7sLJI4wcG8n0gpRTd.7amOft4bAJPUrgAd5roGJTy.fQZSYeO', NULL, NULL, 1, 1, NULL, 1, 0, 1, '2020-06-19 12:08:59', 1, '2020-08-06 09:53:43', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sp_user_app_role
-- ----------------------------
DROP TABLE IF EXISTS `sp_user_app_role`;
CREATE TABLE `sp_user_app_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `app_id` bigint(20) DEFAULT NULL COMMENT '应用ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与应用与角色对应关系';

-- ----------------------------
-- Table structure for sp_version
-- ----------------------------
DROP TABLE IF EXISTS `sp_version`;
CREATE TABLE `sp_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `platform` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '平台名称',
  `version_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '版本名称',
  `version_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '版本号',
  `update_log` varchar(500) DEFAULT NULL COMMENT '更新日志',
  `download_url` varchar(500) DEFAULT NULL COMMENT '下载地址',
  `internal_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '内部下载url',
  `external_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '外部下载url',
  `version_config` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '版本配置（需要版本更新的版本号例如 10,11,12）',
  `enable_edit` int(1) NOT NULL DEFAULT '1' COMMENT '是否可编辑',
  `enable_flag` int(1) NOT NULL DEFAULT '1' COMMENT '启用状态',
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `need_update_versions` longtext CHARACTER SET utf8 COLLATE utf8_bin COMMENT '版本配置（需要版本更新的版本号例如: 1.1.1,1.1.2,1.1.3）',
  `canary_release_stage` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '发布阶段（例如 2,10,30,40,50,100,100）',
  `canary_release_enable` int(1) NOT NULL DEFAULT '0' COMMENT '0--已禁用 1--已启用  是否灰度发布 默认禁用',
  `canary_release_use_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '灰度发已用时间（毫秒）',
  `old_canary_release_use_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '已用时间（用于启用一段时间关闭的状态保存）',
  `enable_time` datetime DEFAULT NULL COMMENT '启用时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='版本管理';


SET FOREIGN_KEY_CHECKS = 1;
