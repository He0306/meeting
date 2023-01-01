/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : rookiemeeting

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 08/09/2022 18:31:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `departmentid` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门编号',
  `departmentname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`departmentid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '技术部');
INSERT INTO `department` VALUES (2, '人事部');
INSERT INTO `department` VALUES (3, '财务部');
INSERT INTO `department` VALUES (4, '行政部');
INSERT INTO `department` VALUES (5, '运维部');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `employeeid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID（唯一）',
  `employeename` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名（登录的账号）',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（0未审批 1审批通过 2审批未通过）',
  `departmentid` int(16) NULL DEFAULT NULL COMMENT '部门编号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `role` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色（1普通用户 2管理员）',
  `faceimg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人脸照片',
  `is_delete` bit(1) NULL DEFAULT b'0' COMMENT '逻辑删除',
  PRIMARY KEY (`employeeid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '管理员', 'admin', '18773283353', '2740860037@qq.com', 1, 1, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_ADMIN', '2022-08-29-10-55-06.1.jpg', b'0');
INSERT INTO `employee` VALUES (2, '林耀坤', 'linyk', '13685026987', 'yang@qq.com', 1, 2, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', '', b'0');
INSERT INTO `employee` VALUES (3, '熊杰文', 'xiongjw', '15863254788', 'xiong@qq.com', 1, 3, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (4, '王敏', 'wangmin', '15632598757', 'wangm@qq.com', 1, 4, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (5, '黄美玲', 'huangml', '13563589624', 'huangml@qq.com', 1, 4, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (6, '黄美玲2', 'huangml002', 'huangml@qq.com', '13567898765', 2, 1, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (7, '王敏2', 'wangmin002', '13454332334', 'wang@qq.com', 2, 4, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (8, '陈敏', 'chenm', '13559994444', 'www@aa.com', 1, 2, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (9, '陈晨', 'wangm', '12345678998', 'qqq@qq.com', 2, 1, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (11, '张三', 'zhangsan', '15632469878', 'zhan@qq.com', 1, 4, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (12, 'kxf', 'kxf', '123', '123@qq.com', 1, 1, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'0');
INSERT INTO `employee` VALUES (13, '王卫红', 'hec', '17680881144', '2236281139@qq.com', 0, 2, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', '', b'0');
INSERT INTO `employee` VALUES (14, '何超', 'hechao', '18773283353', '2740860037@qq.com', 1, 1, '20a95ceec8c7c414a890eedf942afcc8', 'ROLE_NORMAL', NULL, b'1');

-- ----------------------------
-- Table structure for meeting
-- ----------------------------
DROP TABLE IF EXISTS `meeting`;
CREATE TABLE `meeting`  (
  `meetingid` int(11) NOT NULL AUTO_INCREMENT COMMENT '会议ID',
  `meetingname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会议名称',
  `roomid` int(11) NULL DEFAULT NULL COMMENT '房间号',
  `reservationistid` bigint(11) NULL DEFAULT NULL COMMENT '预订会议人的ID',
  `numberofparticipants` int(11) NULL DEFAULT NULL COMMENT '参加人数',
  `signinstarttime` datetime(0) NULL DEFAULT NULL COMMENT '会议开始签到时间',
  `signinendtime` datetime(0) NULL DEFAULT NULL COMMENT '会议结束签到时间',
  `starttime` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `endtime` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `reservationtime` datetime(0) NULL DEFAULT NULL COMMENT '预约时间',
  `canceledtime` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会议说明',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（0没取消的会议 1取消的会议）',
  `canceledreason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取消会议原因',
  PRIMARY KEY (`meetingid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting
-- ----------------------------
INSERT INTO `meeting` VALUES (1, '测测', 1, 8, 20, '2022-06-06 12:28:00', '2022-06-07 12:28:00', '2022-06-06 12:28:00', '2022-06-07 13:28:00', '2022-01-05 12:28:00', '2022-06-05 12:06:20', '啦啦啦', 1, '有事');
INSERT INTO `meeting` VALUES (2, '我看看', 2, 13, 12, '2022-06-08 14:46:06', '2022-06-08 15:00:06', '2022-06-08 15:06:06', '2022-06-08 20:06:06', '2022-06-07 15:06:06', '2022-06-08 15:06:06', '我看看', 1, '有事');
INSERT INTO `meeting` VALUES (3, '运营会', 5, 3, 12, '2022-08-01 15:40:00', '2022-08-01 15:55:00', '2022-08-01 16:00:00', '2022-08-01 17:00:00', '2022-07-31 17:44:10', NULL, '测试', 0, NULL);
INSERT INTO `meeting` VALUES (4, '市场部会议', 6, 8, 12, '2022-08-12 13:45:00', '2022-08-12 13:55:00', '2022-08-12 14:00:00', '2022-08-12 15:00:00', '2022-08-11 13:20:23', NULL, '市场部', 0, NULL);
INSERT INTO `meeting` VALUES (5, '内部会议', 5, 2, 12, '2022-08-03 09:45:00', '2022-08-03 09:55:00', '2022-08-03 10:00:00', '2022-08-03 11:00:00', '2022-08-02 15:45:45', NULL, '内部会议', 0, NULL);
INSERT INTO `meeting` VALUES (6, '我的会议1', 6, 8, 12, '2022-08-04 09:45:00', '2022-08-04 09:59:00', '2022-08-04 10:00:00', '2022-08-04 11:00:00', '2022-08-03 13:58:11', NULL, '测试', 0, '');
INSERT INTO `meeting` VALUES (8, '哈哈', 6, 8, 12, '2022-06-12 13:40:45', '2022-06-12 13:58:45', '2022-06-12 14:00:45', '2022-06-12 15:00:45', '2022-06-10 14:00:45', '2022-06-11 14:00:45', '你好', 1, '有事');
INSERT INTO `meeting` VALUES (10, '我的会议', 2, 8, 12, '2022-07-31 18:05:30', '2022-08-01 13:58:00', '2022-08-01 14:00:00', '2022-08-01 15:00:00', '2022-07-31 10:25:52', NULL, '会议测试', 0, NULL);
INSERT INTO `meeting` VALUES (11, '我问问', 6, 1, 12, '2022-06-11 13:40:03', '2022-06-11 14:00:00', '2022-06-11 14:00:03', '2022-06-11 15:00:03', '2022-06-08 09:00:03', '2022-06-09 14:00:03', '地点', 1, '没理由');
INSERT INTO `meeting` VALUES (12, '我的会议4', 3, 3, 12, '2022-08-02 08:45:00', '2022-08-02 08:58:00', '2022-08-02 09:00:00', '2022-08-02 10:00:00', '2022-08-01 10:21:32', NULL, '我的会议', 0, NULL);
INSERT INTO `meeting` VALUES (13, '班会', 3, 8, 12, '2022-05-16 14:35:25', '2022-05-16 15:00:00', '2022-05-16 15:00:25', '2022-05-16 16:00:25', '2022-05-14 15:00:25', '2022-05-15 11:00:25', '班会', 1, '有事');
INSERT INTO `meeting` VALUES (14, '测试会议', 6, 1, 12, '2022-06-10 08:10:11', '2022-06-10 08:25:11', '2022-06-10 08:30:11', '2022-06-10 09:30:11', '2022-06-07 08:30:11', '2022-06-09 10:11:11', 'ss1', 1, '没理由');
INSERT INTO `meeting` VALUES (15, '206会议', 5, 6, 4, '2022-06-11 09:40:00', '2022-06-11 10:00:00', '2022-06-11 10:00:00', '2022-06-11 11:30:00', '2022-06-09 10:30:01', '2022-06-10 11:15:55', '开会', 1, '没理由');
INSERT INTO `meeting` VALUES (16, '206会议', 5, 5, 4, '2022-07-29 12:50:00', '2022-07-29 12:58:00', '2022-07-29 13:00:00', '2022-07-29 15:00:00', '2022-07-27 10:00:00', NULL, '206大会', 0, NULL);
INSERT INTO `meeting` VALUES (17, '206会议', 5, 1, 4, '2022-07-30 10:00:00', '2022-07-30 10:09:00', '2022-07-30 10:10:00', '2022-07-30 11:10:00', '2022-07-29 10:02:00', NULL, '测测', 0, NULL);
INSERT INTO `meeting` VALUES (19, '测试', 3, 2, 12, '2022-08-01 09:12:00', '2022-08-01 09:19:00', '2022-08-01 09:20:00', '2022-08-01 10:20:00', '2022-08-01 08:20:00', NULL, '111', 0, NULL);
INSERT INTO `meeting` VALUES (21, '测试测试', 3, 1, 12, '2022-08-02 13:45:39', '2022-08-03 00:00:00', '2022-08-02 13:45:44', '2022-08-03 00:00:00', '2022-11-02 13:45:55', NULL, '第一个会议', 0, NULL);

-- ----------------------------
-- Table structure for meetingparticipants
-- ----------------------------
DROP TABLE IF EXISTS `meetingparticipants`;
CREATE TABLE `meetingparticipants`  (
  `meetingid` int(11) NOT NULL COMMENT '会议ID',
  `employeeid` bigint(20) NULL DEFAULT NULL COMMENT '参加会议的员工的员工ID',
  `status` int(11) NULL DEFAULT NULL COMMENT '签到状态，0未签到，1已签到',
  `checkintime` datetime(0) NULL DEFAULT NULL COMMENT '签到时间',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签到地点'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meetingparticipants
-- ----------------------------
INSERT INTO `meetingparticipants` VALUES (1, 1, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (2, 1, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (3, 3, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (1, 5, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (3, 6, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (4, 7, 1, '2022-06-09 15:54:14', NULL);
INSERT INTO `meetingparticipants` VALUES (5, 9, 1, '2022-06-12 20:17:53', '湖南省湘潭市');
INSERT INTO `meetingparticipants` VALUES (6, 10, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (8, 13, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (11, 6, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (12, 2, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (13, 5, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (12, 3, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (14, 13, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (12, 9, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (15, 11, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (16, 12, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (17, 9, 1, '2022-07-27 18:17:00', '湖南省郴州市');
INSERT INTO `meetingparticipants` VALUES (17, 1, 1, '2022-07-27 18:17:10', '湖南省郴州市');
INSERT INTO `meetingparticipants` VALUES (17, 2, 1, '2022-07-28 11:16:47', '湖南省郴州市');
INSERT INTO `meetingparticipants` VALUES (17, 3, 1, '2022-08-01 10:03:36', '湖南省');
INSERT INTO `meetingparticipants` VALUES (17, 4, 1, '2022-08-01 17:44:59', '湖南省');
INSERT INTO `meetingparticipants` VALUES (17, 8, 1, '2022-08-01 10:04:32', '湖南省');
INSERT INTO `meetingparticipants` VALUES (17, 13, 1, '2022-07-28 11:16:50', '湖南省郴州市');
INSERT INTO `meetingparticipants` VALUES (38, 1, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (38, 4, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (38, 13, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (14, 4, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (38, 3, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (38, 2, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (14, 8, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (14, 3, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (14, 12, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (5, 1, 1, '2022-06-12 20:20:56', '湖南省湘潭市');
INSERT INTO `meetingparticipants` VALUES (6, 1, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (5, 12, 1, '2022-06-12 20:21:12', '湖南省湘潭市');
INSERT INTO `meetingparticipants` VALUES (41, 2, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (41, 3, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (41, 1, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (10, 1, 0, NULL, NULL);
INSERT INTO `meetingparticipants` VALUES (21, 1, 1, '2022-08-02 13:46:15', '湖南省');

-- ----------------------------
-- Table structure for meetingroom
-- ----------------------------
DROP TABLE IF EXISTS `meetingroom`;
CREATE TABLE `meetingroom`  (
  `roomid` int(11) NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
  `roomnum` int(11) NOT NULL COMMENT '会议室房间号',
  `roomname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '会议室名称',
  `capacity` int(11) NULL DEFAULT NULL COMMENT '可容纳人数',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态（0可用 1被占用）',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`roomid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meetingroom
-- ----------------------------
INSERT INTO `meetingroom` VALUES (1, 101, '第一会议室', 15, 1, '公共会议室');
INSERT INTO `meetingroom` VALUES (2, 102, '第二会议室', 5, 0, '管理部门会议室');
INSERT INTO `meetingroom` VALUES (3, 103, '第三会议室', 12, 0, '市场部专用会议室');
INSERT INTO `meetingroom` VALUES (4, 401, '第四会议室', 15, 0, '公共会议室');
INSERT INTO `meetingroom` VALUES (5, 201, '第五会议室', 15, 0, '最大会议室');
INSERT INTO `meetingroom` VALUES (6, 601, '第六会议室', 12, 0, '需要提前三天预定');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'el-icon-user', 'el-icon-user', 'icon');
INSERT INTO `sys_dict` VALUES (2, 'el-icon-house', 'el-icon-house', 'icon');
INSERT INTO `sys_dict` VALUES (3, 'el-icon-menu', 'el-icon-menu', 'icon');
INSERT INTO `sys_dict` VALUES (4, 'el-icon-s-custom', 'el-icon-s-custom', 'icon');
INSERT INTO `sys_dict` VALUES (5, 'el-icon-documents', 'el-icon-document', 'icon');
INSERT INTO `sys_dict` VALUES (6, 'el-icon-setting', 'el-icon-setting', 'icon');
INSERT INTO `sys_dict` VALUES (7, 'el-icon-map-location', 'el-icon-map-location', 'icon');
INSERT INTO `sys_dict` VALUES (8, 'el-icon-s-claim', 'el-icon-s-claim', 'icon');
INSERT INTO `sys_dict` VALUES (12, 'el-icon-share', 'el-icon-share', 'icon');
INSERT INTO `sys_dict` VALUES (13, 'el-icon-s-marketing', 'el-icon-s-marketing', 'icon');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路劲',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父级ID',
  `page_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '页面路径',
  `sort_num` int(11) NULL DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '主页', '/home', 'el-icon-s-home', '主页', NULL, 'Home', 1);
INSERT INTO `sys_menu` VALUES (2, '个人中心', NULL, 'el-icon-user', '个人中心', NULL, NULL, 3);
INSERT INTO `sys_menu` VALUES (3, '已取消的会议', '/cancel', '', NULL, 2, 'Personal Center/Cancel', NULL);
INSERT INTO `sys_menu` VALUES (4, '我的预定', '/scheduled', '', NULL, 2, 'Personal Center/Scheduled', NULL);
INSERT INTO `sys_menu` VALUES (5, '我的会议', '/meeting', '', NULL, 2, 'Personal Center/Meeting', NULL);
INSERT INTO `sys_menu` VALUES (6, '会议预定', '', 'el-icon-s-claim', '会议预定', NULL, '', 4);
INSERT INTO `sys_menu` VALUES (7, '预定会议', '/bookScheduled', '', NULL, 6, 'Meeting booking/BookScheduled', NULL);
INSERT INTO `sys_menu` VALUES (8, '搜索会议', '/searchMeeting', '', NULL, 6, 'Meeting booking/SearchMeeting', NULL);
INSERT INTO `sys_menu` VALUES (9, '系统管理员', NULL, 'el-icon-s-custom', '系统管理员', NULL, NULL, 5);
INSERT INTO `sys_menu` VALUES (10, '注册审批', '/approval', NULL, NULL, 9, 'Personnel management/Approval', NULL);
INSERT INTO `sys_menu` VALUES (11, '部门管理', '/manage', NULL, NULL, 9, 'Personnel management/Manage', NULL);
INSERT INTO `sys_menu` VALUES (12, '员工管理', '/search', NULL, NULL, 9, 'Personnel management/Search', NULL);
INSERT INTO `sys_menu` VALUES (13, '会议室管理', '/meetingRoom', NULL, NULL, 9, 'Meeting booking/MeetingRoom', NULL);
INSERT INTO `sys_menu` VALUES (14, '权限管理', NULL, 'el-icon-share', NULL, NULL, NULL, 6);
INSERT INTO `sys_menu` VALUES (15, '角色管理', '/role', NULL, NULL, 14, 'sys/Role', NULL);
INSERT INTO `sys_menu` VALUES (16, '菜单管理', '/menu', NULL, NULL, 14, 'sys/Menu', NULL);
INSERT INTO `sys_menu` VALUES (17, '图标管理', '/dict', NULL, NULL, 14, 'sys/Dict', NULL);
INSERT INTO `sys_menu` VALUES (22, '数据报表', '/dataReport', 'el-icon-s-marketing', '数据报表', NULL, 'DataReport/DataReport', 2);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `flag` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', '管理员', 'ROLE_ADMIN');
INSERT INTO `sys_role` VALUES (2, '普通用户', '普通用户', 'ROLE_NORMAL');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 6);
INSERT INTO `sys_role_menu` VALUES (1, 7);
INSERT INTO `sys_role_menu` VALUES (1, 8);
INSERT INTO `sys_role_menu` VALUES (1, 9);
INSERT INTO `sys_role_menu` VALUES (1, 10);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (1, 13);
INSERT INTO `sys_role_menu` VALUES (1, 14);
INSERT INTO `sys_role_menu` VALUES (1, 15);
INSERT INTO `sys_role_menu` VALUES (1, 16);
INSERT INTO `sys_role_menu` VALUES (1, 17);
INSERT INTO `sys_role_menu` VALUES (1, 22);
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 5);
INSERT INTO `sys_role_menu` VALUES (2, 6);
INSERT INTO `sys_role_menu` VALUES (2, 7);
INSERT INTO `sys_role_menu` VALUES (2, 8);

SET FOREIGN_KEY_CHECKS = 1;
