DROP TABLE IF EXISTS `bom_history`;

DROP TABLE IF EXISTS `search_history`;

DROP TABLE IF EXISTS `extra_property_data`;

CREATE TABLE `bom_history` (
  `creation_date` datetime DEFAULT current_timestamp() ,
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
  `bom_view`  VARCHAR(128) NULL  DEFAULT NULL ,
  `rev_uid`  VARCHAR(64) NULL  DEFAULT NULL ,
  `rev_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'bom查询历史';
CREATE TABLE `search_history` (
  `creation_date` datetime DEFAULT current_timestamp() ,
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
  `app_code`  VARCHAR(128) NULL  DEFAULT NULL ,
  `condition_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `combin_condition`  VARCHAR(1024) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE  
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '查询条件历史';



CREATE TABLE `extra_property_data` (
  `creation_date` datetime DEFAULT current_timestamp() ,
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
  `property`  VARCHAR(64) NULL  DEFAULT NULL ,
  `value`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '扩展字段数据';