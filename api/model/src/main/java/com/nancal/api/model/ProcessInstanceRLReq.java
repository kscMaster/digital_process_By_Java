package com.nancal.api.model;

import com.nancal.api.model.common.RelationReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "ProcessInstanceRL 流程实例和工作对象关系 的请求,左对象为流程实例ID，右对象为工作对象")
public class ProcessInstanceRLReq extends RelationReq {
    @Override
    public String getRelationType() {
        return "ProcessInstanceRL";
    }

    @Override
    public String getObjectType() {
        return "ProcessInstanceRL";
    }
}
