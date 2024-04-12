ALTER TABLE `lzdigit_rd_624`.`gte4tech_document_revision`
    ADD COLUMN `gte4file_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件号' AFTER `part_no`,
ADD COLUMN `gte4designer` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设计者' AFTER `gte4file_no`;