package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "生成PBOM校验 响应")
public class EBOMCheckResp {
    @ApiModelProperty(value = "成功数")
    private String success;

    @ApiModelProperty(value = "失败数")
    private String fail;

    @ApiModelProperty(value = "失败日志")
    private String log;

    @ApiModelProperty(value = "对应PBOM的uid")
    private String uid;

    @ApiModelProperty(value = "对应PBOM的对象类型")
    private String objectType;
}
