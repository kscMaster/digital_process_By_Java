ALTER TABLE `lzdigit_rd_624`.`bom_history`
    ADD COLUMN `revision_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号' AFTER `rev_object_type`;