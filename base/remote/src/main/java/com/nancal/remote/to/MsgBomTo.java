package com.nancal.remote.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BOPView BOP视图 的返回")
public class MsgBomTo implements Serializable {


    @ApiModelProperty(value = "主键id")
    private String uid;

    @ApiModelProperty(value = "对象类型")
    private String objectType;
}
