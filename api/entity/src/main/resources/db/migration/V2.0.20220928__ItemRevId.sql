ALTER TABLE `lzdigit_rd_624`.`change_beforerl`
    ADD COLUMN `item_rev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '版本号' ;
ALTER TABLE `lzdigit_rd_624`.`change_afterrl`
    ADD COLUMN `item_rev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '版本号' ;
ALTER TABLE `lzdigit_rd_624`.`change_effectrl`
    ADD COLUMN `item_rev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '版本号' ;