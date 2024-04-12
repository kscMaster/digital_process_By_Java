ALTER TABLE `lzdigit_rd_624`.`masterrl`
ADD INDEX `index_left`(`left_object`, `left_object_type`, `right_object`, `right_object_type`);

ALTER TABLE `lzdigit_rd_624`.`bomnode`
ADD INDEX `index_parent`(`parent_item`, `parent_item_type`);


ALTER TABLE `lzdigit_rd_624`.`library_folderrl`
ADD INDEX `index_left`(`left_object`, `left_object_type`, `right_object`, `right_object_type`);


ALTER TABLE `lzdigit_rd_624`.`dataset_filerl`
ADD INDEX `index_left`(`left_object`, `left_object_type`);


ALTER TABLE `lzdigit_rd_624`.`specificationrl`
ADD INDEX `index_left`(`left_object`, `left_object_type`);


ALTER TABLE `lzdigit_rd_624`.`gte4part_revision`
ADD INDEX `index_itemId`(`item_id`);


ALTER TABLE `lzdigit_rd_624`.`gte4process_revision`
ADD INDEX `index_itemId`(`item_id`);


ALTER TABLE `lzdigit_rd_624`.`tool_revision`
ADD INDEX `index_itemId`(`item_id`);


ALTER TABLE `lzdigit_rd_624`.`measure_revision`
ADD INDEX `index_itemId`(`item_id`);


ALTER TABLE `lzdigit_rd_624`.`auxiliary_material_revision`
ADD INDEX `index_itemId`(`item_id`);

ALTER TABLE `lzdigit_rd_624`.`equipment_revision`
ADD INDEX `index_itemId`(`item_id`);
