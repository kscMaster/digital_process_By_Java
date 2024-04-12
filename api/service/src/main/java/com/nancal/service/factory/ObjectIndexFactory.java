package com.nancal.service.factory;

import com.nancal.model.entity.ItemEntity;
import com.nancal.model.entity.ItemRevisionEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.bo.ObjectIndex;
import org.springframework.stereotype.Component;

@Component
public class ObjectIndexFactory {

    /***
     * 创建对象索引
     *
     * @param workspaceObjectEntity 入参对象
     * @author 徐鹏军
     * @date 2022/4/14 10:31
     * @return {@link ObjectIndex}
     */
    public ObjectIndex create(WorkspaceObjectEntity workspaceObjectEntity) {
        ObjectIndex objectIndex = new ObjectIndex();
        objectIndex.setObjectUid(workspaceObjectEntity.getUid());
        objectIndex.setObjectType(workspaceObjectEntity.getObjectType());
        objectIndex.setObjectName(workspaceObjectEntity.getObjectName());
        if (workspaceObjectEntity instanceof ItemRevisionEntity) {
            ItemRevisionEntity revision = (ItemRevisionEntity) workspaceObjectEntity;
            objectIndex.setItemId(revision.getItemId());
        }
        if (workspaceObjectEntity instanceof ItemEntity) {
            ItemEntity item = (ItemEntity) workspaceObjectEntity;
            objectIndex.setItemId(item.getItemId());
        }
        return objectIndex;
    }
}
