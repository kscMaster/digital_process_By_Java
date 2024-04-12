ALTER TABLE `lzdigit_rd_624`.`gte4mfg_step`
    ADD COLUMN `is_execute` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已执行' AFTER `item_id`;