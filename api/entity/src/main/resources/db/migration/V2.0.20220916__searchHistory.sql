ALTER TABLE `lzdigit_rd_624`.`search_history`
    MODIFY COLUMN `creation_date` datetime(0) NULL DEFAULT NULL FIRST,
    ADD COLUMN `condition_object_type` varchar(128) NULL COMMENT '条件对象类型' AFTER `combin_condition`;