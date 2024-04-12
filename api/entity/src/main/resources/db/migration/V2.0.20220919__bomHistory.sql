ALTER TABLE `lzdigit_rd_624`.`bom_history`
    ADD COLUMN `item_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号' ;


ALTER TABLE `lzdigit_rd_624`.`gte4workline_revision`
ADD COLUMN `gte4line_type` varchar(128) NULL COMMENT '线体类型' AFTER `gte4line_capacity`;