package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "MfgOperationRevision 制造工序版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MfgOperationRevisionReq extends MsgbImportReq  implements Serializable{

    @ApiModelProperty(value="工序类型",required=true)
    @Length(max = 64)
    private String opType;

    @ApiModelProperty(value="是否关键工序",required=false,example="false")
    private Boolean isKey;
}