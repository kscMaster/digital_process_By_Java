ALTER TABLE `lzdigit_rd_624`.`gte4part_revision`
    MODIFY COLUMN `del_flag` bit(1) NOT NULL DEFAULT b'0' AFTER `uid`;