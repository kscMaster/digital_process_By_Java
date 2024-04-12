package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "MsgbImportReq 工艺规划导入公共继承类")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MsgbImportReq extends ItemRevisionReq  implements Serializable{


    @ApiModelProperty(value="数量",required=false)
    @FieldAlias(name = "数量")
    private Double quantity;

    @ApiModelProperty(value="查找编号")
    @FieldAlias(name = "查找编号")
    private Integer foundNo;

    @ApiModelProperty(value="位号")
    @FieldAlias(name = "位号")
    private String tagNo;

    @FieldAlias(name = "数据类型",required = true)
    private String dataType;

    @FieldAlias(name = "层级",required = true)
    private String rank;

    @ApiModelProperty(value="存储子级")
    private List<MsgbImportReq> children;

}