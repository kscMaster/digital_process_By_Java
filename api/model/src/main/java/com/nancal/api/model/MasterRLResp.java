package com.nancal.api.model;

import com.nancal.api.model.common.RelationResp;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "MasterRL 零组件和版本的关系 的响应")
public class MasterRLResp extends RelationResp {

    @Override
    public String getRelationType() {
        return "MasterRL";
    }
}
