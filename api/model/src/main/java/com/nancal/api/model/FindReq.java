package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "find EBOM首页查询的请求")
public class FindReq implements Serializable {

    @ApiModelProperty(value = "主键id",required = true)
    @Length(max = 64, message = "id超过了最大长度限制")
    @NotBlank(message = "主键id不能为空")
    private String uid;

    @ApiModelProperty(value = "对象类型",required = true)
    @Length(max = 64, message = "对象类型超过了最大长度限制")
    @NotBlank(message = "类型不能为空")
    private String objectType;

    @ApiModelProperty(value = "深度")
    private Integer deep=1;

    @ApiModelProperty(value = "bom行id")
    private String bomId;

    @ApiModelProperty(value = "是否快照")
    private boolean isSnapshot=false;

    @ApiModelProperty(value = "快照uid")
    private String bomSnapshotUid;

    public FindReq(String uid,String objectType, Integer deep) {
        this.uid = uid;
        this.objectType = objectType;
        this.deep = deep;
    }
}
