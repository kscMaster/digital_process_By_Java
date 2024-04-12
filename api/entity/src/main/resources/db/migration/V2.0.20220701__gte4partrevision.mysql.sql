ALTER TABLE `lzdigit_rd_624`.`gte4part_revision`
    ADD COLUMN `gte4init_model` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '初始型号' ,
ADD COLUMN `gte4_phase` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '阶段标识';
