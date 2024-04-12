package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@ApiModel(value = "MfgOperationRevision 制造工序版本 的响应")
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MfgOperationRevisionResp extends ItemRevisionResp  implements Serializable{
    @JsonDict(DictConstant.OP_TYPE)
    @ApiModelProperty(value="工序类型",required=false)
    private String opType;
    @ApiModelProperty(value="是否关键工序",required=false,example="false")
    @JsonDict(DictConstant.BOOLEAN_TYPE)
    private Boolean isKey;
}