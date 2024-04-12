package com.nancal.service.bo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.FileStorageResp;
import com.nancal.common.base.IdRequest;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.DatasetFileRLEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.service.factory.DatasetFileRLFactory;
import com.nancal.service.factory.MasterRLFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(value = "Dataset 数据集 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Dataset extends WorkspaceObject {

    @ApiModelProperty(value = "文件名称")
    private List<String> refName;

    @ApiModelProperty(value = "文件类型")
    private List<String> refType;


    /***
     * 获取文件列表
     *
     * @param id 请求参数
     * @author 徐鹏军
     * @date 2022/4/12 15:26
     * @return {@link List <? extends BusinessObjectResp >}
     */
    public List<? extends BusinessObjectResp> fileList(IdRequest id) {
        DatasetFileRLFactory datasetFileRLFactory = SpringUtil.getBean(DatasetFileRLFactory.class);
        List<FileStorage> entities = datasetFileRLFactory.create().getFileStorages(Collections.singletonList(id.getUid()));
        if (CollUtil.isEmpty(entities)) {
            return Collections.emptyList();
        }
        FileStorage fileStorage = CollUtil.getFirst(entities);
        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
        String objectType = new DatasetFileRLEntity().getObjectType();
        // 通过右id找左对象
        List<WorkspaceObjectEntity> parents = masterRL.getParents(fileStorage.getUid(), objectType, null);
        if (CollUtil.isEmpty(parents)) {
            return Collections.emptyList();
        }
        WorkspaceObjectEntity leftEntity = CollUtil.getFirst(parents);
        FileStorageResp resp = new FileStorageResp();
        BeanUtil.copyPropertiesIgnoreNull(fileStorage, resp);
        resp.setObjectName(leftEntity.getObjectName());
        resp.setObjectDesc(leftEntity.getObjectDesc());
        return Collections.singletonList(resp);
    }


}