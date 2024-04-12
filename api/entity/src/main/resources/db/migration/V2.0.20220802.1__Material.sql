CREATE TABLE `gte4material_rl` (
  `relation_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `left_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object`  VARCHAR(64) NULL  DEFAULT NULL ,
  `right_object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_date`  DATE NULL  DEFAULT NULL ,
  `creation_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `creation_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `last_update`  DATE NULL  DEFAULT NULL ,
  `last_update_user_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `last_update_username`  VARCHAR(128) NULL  DEFAULT NULL ,
  `owner_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `owner_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_name`  VARCHAR(128) NULL  DEFAULT NULL ,
  `object_desc`  VARCHAR(1024) NULL  DEFAULT NULL ,
  `object_type`  VARCHAR(64) NULL  DEFAULT NULL ,
  `life_cycle_state`  VARCHAR(64) NULL  DEFAULT NULL ,
  `state_change_date`  DATE NULL  DEFAULT NULL ,
  `remark`  VARCHAR(512) NULL  DEFAULT NULL ,
  `secret_level`  VARCHAR(128) NULL  DEFAULT NULL ,
  `secret_term`  VARCHAR(128) NULL  DEFAULT NULL ,
  `tenant_id`  VARCHAR(64) NULL  DEFAULT NULL ,
  `uid`  VARCHAR(64) NOT NULL ,
  `del_flag`  BIT(1) NULL  DEFAULT NULL ,
 PRIMARY KEY (`uid`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '设计零组件版本与物料零件版本的关系';


ALTER TABLE `lzdigit_rd_624`.`gte4material_revision`
ADD COLUMN `gte4rated_life_time`  VARCHAR(64) NULL  DEFAULT NULL AFTER `gte4avail_material`;
ALTER TABLE `lzdigit_rd_624`.`gte4material_revision`
ADD COLUMN   `gte4current_operation_time`  VARCHAR(64) NULL  DEFAULT NULL AFTER `gte4rated_life_time`;
ALTER TABLE `lzdigit_rd_624`.`gte4material_revision`
ADD COLUMN   `gte4model_no`  VARCHAR(64) NULL  DEFAULT NULL AFTER `gte4current_operation_time`;

ALTER TABLE `lzdigit_rd_624`.`bomnode`
    ADD COLUMN `is_take_item` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否集件' AFTER `parent_item_rev_id`;