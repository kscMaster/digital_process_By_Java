package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@ApiModel(value = "变更单获取对象的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeObjectResp<T extends WorkspaceObjectResp> {
    @ApiModelProperty(value="变更后数据",required=false)
    List<T> afterList;
    @ApiModelProperty(value="变更前数据",required=false)
    List<T> beforeList;
    @ApiModelProperty(value="受影响的数据",required=false)
    List<T> effectedList;
}
