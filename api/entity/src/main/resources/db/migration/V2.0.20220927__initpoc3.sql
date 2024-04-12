DROP TABLE IF EXISTS `change_afterrl`;

DROP TABLE IF EXISTS `change`;

DROP TABLE IF EXISTS `gte4dist_org_entry`;

DROP TABLE IF EXISTS `gte4dist_orgrl`;

DROP TABLE IF EXISTS `gte4notic_org_entry`;

DROP TABLE IF EXISTS `gte4notic_orgrl`;

DROP TABLE IF EXISTS `gte4part_change`;

DROP TABLE IF EXISTS `change_effectrl`;

DROP TABLE IF EXISTS `gte4responsible_rl`;

DROP TABLE IF EXISTS `msg_template`;

CREATE TABLE `change_afterrl` (
  `relation_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `change_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `change_reason`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `wip_suggestion`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `processed_suggestion`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `change_opinion`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `effective_date`  DATE NULL  DEFAULT NULL ,
  `change_at_once`  VARCHAR(1024) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `change` (
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `change_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `change_comment`  VARCHAR(1024) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gte4dist_org_entry` (
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `gte4distri_org`  VARCHAR(128) NULL  DEFAULT NULL ,
  `gte4distri_org_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `gte4org_responsible_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `gte4org_responsible_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `gte4responsible_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `gte4responsible_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `gte4wip_suggestion`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `gte4processed_suggestion`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `gte4change_opinion`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `gte4effective_date`  DATE NULL  DEFAULT NULL ,
  `gte4notice_date`  DATE NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gte4dist_orgrl` (
  `relation_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gte4notic_org_entry` (
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `gte4notice_org`  VARCHAR(128) NULL  DEFAULT NULL ,
  `gte4notice_org_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `gte4notice_date`  DATE NULL  DEFAULT NULL ,
  `gte4check`  BIT(1) NULL  DEFAULT NULL ,
  `gte4check_time`  DATE NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gte4notic_orgrl` (
  `relation_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gte4part_change` (
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `change_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `change_comment`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `gte4change_type`  VARCHAR(128) NULL  DEFAULT NULL ,
  `gte4change_reason`  VARCHAR(1024) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `change_effectrl` (
  `relation_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
 PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gte4responsible_rl` (
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `gte4left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `gte4lobject_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `gte4responsible_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `gte4responsible_id`  VARCHAR(64) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '负责人关系表';


CREATE TABLE `msg_template` (
  `creation_date` datetime DEFAULT current_timestamp(),
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update` datetime DEFAULT current_timestamp(),
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state` varchar(64) DEFAULT 'Working',
  `state_change_date` datetime DEFAULT current_timestamp(),
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag` bit(1) NOT NULL DEFAULT b'0',
  `title`  VARCHAR(128) NULL  DEFAULT NULL ,
  `body`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `msg_type`  INTEGER NULL  DEFAULT NULL ,
  `msg_code`  VARCHAR(128) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '消息模板';



ALTER TABLE `change_afterrl`
MODIFY COLUMN `effective_date` datetime(0) NULL DEFAULT NULL AFTER `change_opinion`;


ALTER TABLE `gte4dist_org_entry`
MODIFY COLUMN `gte4effective_date` datetime(0) NULL DEFAULT NULL AFTER `gte4change_opinion`;


ALTER TABLE `gte4notice_org_entry`
MODIFY COLUMN `gte4notice_date` datetime(0) NULL DEFAULT NULL AFTER `gte4notice_org_id`,
MODIFY COLUMN `gte4check_time` datetime(0) NULL DEFAULT NULL AFTER `gte4check`;