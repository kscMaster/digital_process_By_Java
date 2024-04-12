package com.nancal.api.model;

import com.nancal.api.model.common.RelationReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "SpecificationRL 规范关系 的请求")
public class SpecificationRLReq extends RelationReq {

    @Override
    public String getRelationType() {
        return "SpecificationRL";
    }
}
