
ALTER TABLE `lzdigit_rd_624`.`bomnode`
ADD COLUMN `parent_item_rev_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' AFTER `bom_relation_view`;