ALTER TABLE `lzdigit_rd_624`.`gte4mfg_operation_revision`
    ADD COLUMN `gte4action_capability_group` varchar(128) NULL COMMENT '工艺能力组' AFTER `gte4task_time`;
ALTER TABLE `lzdigit_rd_624`.`gte4mfg_operation_revision`
    ADD COLUMN `gte4action_capability_group_name` varchar(128) NULL COMMENT '工艺能力组名称' AFTER `gte4action_capability_group`;
ALTER TABLE `lzdigit_rd_624`.`gte4mfg_operation_revision`
    ADD COLUMN `gte4board_surface` varchar(128) NULL COMMENT '板面' AFTER `gte4action_capability_group_name`;