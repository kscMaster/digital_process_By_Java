/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50730
Source Host           : localhost:3306
Source Database       : lenovo_esop

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2022-08-30 17:45:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ga_assemline
-- ----------------------------
DROP TABLE IF EXISTS `ga_assemline`;
CREATE TABLE `ga_assemline` (
        `ID` varchar(19) NOT NULL,
        `TASKID` varchar(19) NOT NULL,
        `LINENAME` varchar(128) DEFAULT NULL COMMENT '线体名称',
        `TACTTIME` double DEFAULT NULL COMMENT '线上的瓶颈时间',
        `STATIONNUM` int(11) DEFAULT NULL COMMENT '工位数量',
        `MINSTATIONNUM` int(11) DEFAULT NULL COMMENT '最小工位数',
        `MAXSTATIONNUM` int(11) DEFAULT NULL COMMENT '最大工位数',
        `LINEUID` varchar(32) DEFAULT NULL COMMENT '线体唯一标记',
        PRIMARY KEY (`ID`),
        UNIQUE KEY `ID_UNIQUE` (`ID`),
        KEY `GA_LINE_TASK_FK_idx` (`TASKID`),
        CONSTRAINT `GA_LINE_TASK_FK` FOREIGN KEY (`TASKID`) REFERENCES `ga_task` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ESOP 线体表';

-- ----------------------------
-- Table structure for ga_operation
-- ----------------------------
DROP TABLE IF EXISTS `ga_operation`;
CREATE TABLE `ga_operation` (
        `ID` varchar(19) NOT NULL,
        `LINENAME` varchar(128) DEFAULT NULL COMMENT '线体名称',
        `OPNUM` varchar(16) NOT NULL COMMENT '工序编号',
        `FIXSTATION` int(11) DEFAULT NULL COMMENT '是否关键工序',
        `OPTIME` double DEFAULT NULL COMMENT '工序时间,单位：秒',
        `PREOP` varchar(1024) DEFAULT NULL COMMENT '前一个工序的编号,前一个工序的opnum',
        `TASKID` varchar(19) NOT NULL,
        `OPUID` varchar(32) NOT NULL COMMENT '工序的唯一标记',
        `LINEID` varchar(19) DEFAULT NULL COMMENT '线体id',
        `OPNAME` varchar(256) NOT NULL COMMENT '工序名称',
        `OPID` varchar(32) DEFAULT NULL COMMENT '工序流水号',
        `VOLUMEID` bigint(19) DEFAULT NULL COMMENT '资源id',
        `KEYOPERATION` int(11) NOT NULL DEFAULT '0' COMMENT '是否关键工序',
        `ATTENTION` varchar(1024) DEFAULT NULL COMMENT '注意事项',
        `OPDESC` varchar(128) DEFAULT NULL COMMENT '工序描述',
        `PARENTID` varchar(19) DEFAULT NULL COMMENT '未用到',
        `ISVIRTUAL` int(11) NOT NULL DEFAULT '0' COMMENT '未用到',
        `MATCH_TYPE` int(11) NOT NULL DEFAULT '0' COMMENT 'Matches From Part Links  未用到',
        PRIMARY KEY (`ID`),
        UNIQUE KEY `ID_UNIQUE` (`ID`),
        KEY `GA_OP_TASK_FK_idx` (`TASKID`),
        KEY `GA_OP_PARENT_IDX` (`PARENTID`,`ISVIRTUAL`),
        CONSTRAINT `GA_OP_TASK_FK` FOREIGN KEY (`TASKID`) REFERENCES `ga_task` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ESOP 工序表';

-- ----------------------------
-- Table structure for ga_processstation
-- ----------------------------
DROP TABLE IF EXISTS `ga_processstation`;
CREATE TABLE `ga_processstation` (
 `ID` bigint(19) NOT NULL AUTO_INCREMENT,
 `LINEID` varchar(19) NOT NULL COMMENT '线体id',
 `STATIONNO` int(11) NOT NULL COMMENT '工位序号',
 `STATIONTIME` double(8,2) DEFAULT NULL COMMENT '工位时间',
  `KEYSTATION` int(11) DEFAULT '0' COMMENT '是否关键工位',
  `BOTTLENECK` int(11) DEFAULT '0' COMMENT '是否瓶颈工位',
  `KEYSTATIONCONTENT` varchar(4000) DEFAULT NULL COMMENT '关键工序的名称',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `GA_PS_LINE_FK_idx` (`LINEID`),
  KEY `GA_PS_LINE_IDX` (`LINEID`,`STATIONNO`),
  CONSTRAINT `GA_PS_LINE_FK` FOREIGN KEY (`LINEID`) REFERENCES `ga_assemline` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='ESOP 工位表';

-- ----------------------------
-- Table structure for ga_stationoperation
-- ----------------------------
DROP TABLE IF EXISTS `ga_stationoperation`;
CREATE TABLE `ga_stationoperation` (
       `ID` bigint(19) NOT NULL AUTO_INCREMENT,
       `STATIONID` bigint(19) NOT NULL COMMENT '工位id',
       `OPID` varchar(19) DEFAULT NULL COMMENT 'ga_operation的id',
       `PROCTIME` double DEFAULT NULL COMMENT '工位时间',
       PRIMARY KEY (`ID`),
       UNIQUE KEY `ID_UNIQUE` (`ID`),
       KEY `GA_SO_PS_FK_idx` (`STATIONID`),
       CONSTRAINT `GA_SO_PS_FK` FOREIGN KEY (`STATIONID`) REFERENCES `ga_processstation` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COMMENT='ESOP 工位工序关联表';

-- ----------------------------
-- Table structure for ga_task
-- ----------------------------
DROP TABLE IF EXISTS `ga_task`;
CREATE TABLE `ga_task` (
   `ID` varchar(19) NOT NULL COMMENT '主键id',
   `TASKSTATUS` int(11) NOT NULL DEFAULT '0' COMMENT '任务状态 0:NotStart, 1:Start, 2:Error, 3:Success',
   `UPH` double DEFAULT NULL COMMENT '每小时产量 = 3600 / 节拍(线上的瓶颈时间)',
   `LINEBALANCERATE` double DEFAULT NULL COMMENT '线平衡率',
   `LINETACTTIME` double DEFAULT NULL COMMENT '节拍',
   `TASKLOG` varchar(1024) DEFAULT NULL COMMENT '任务日志',
   `TCUSERID` varchar(128) DEFAULT NULL COMMENT 'tc用户id',
   `TCPASSWORD` varchar(128) DEFAULT NULL COMMENT 'tc密码',
   `FORMUID` varchar(128) DEFAULT NULL COMMENT '没用到',
   `EXPECTEDTACTTIME` double DEFAULT NULL COMMENT '期望节拍，未用到',
   `FIXSTATION` int(11) NOT NULL DEFAULT '0' COMMENT '0：非固定工位，1：固定工位',
   `REV_ID` bigint(19) NOT NULL COMMENT '排产版本id',
   `FROMTC` int(11) DEFAULT '0' COMMENT '0:fromSOP, 1:fromTC',
   `EMPTYSTATION` varchar(64) DEFAULT '9999' COMMENT '空工位数量',
   PRIMARY KEY (`ID`),
   UNIQUE KEY `ID_UNIQUE` (`ID`),
   KEY `GA_TASK_MO_FK_idx` (`REV_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ESOP 任务表';
