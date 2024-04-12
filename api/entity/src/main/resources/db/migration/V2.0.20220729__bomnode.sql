ALTER TABLE `lzdigit_rd_624`.`bomnode`
    MODIFY COLUMN `parent_item_rev` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 0 AFTER `parent_item_type`;