ALTER TABLE `lzdigit_rd_624`.`bomnode`
    ADD COLUMN `manual_task_time` int(11) NULL DEFAULT 0 COMMENT '手动工时' AFTER `tag_no`,
ADD COLUMN `task_time` int(11) NULL DEFAULT 0 COMMENT '总工时' AFTER `manual_tassk_time`;