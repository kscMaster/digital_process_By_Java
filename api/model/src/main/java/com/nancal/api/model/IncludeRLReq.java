package com.nancal.api.model;

import com.nancal.api.model.common.RelationReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "IncludeRL 包含关系 的请求")
public class IncludeRLReq extends RelationReq {

    @Override
    public String getRelationType() {
        return "IncludeRL";
    }
}
