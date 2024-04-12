package com.nancal.api.model;

import com.nancal.common.base.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "ExtraPropertyData 扩展字段数据 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExtraPropertyDataReq extends WorkspaceObjectReq  implements Serializable{

    @ApiModelProperty(value="属性字段",required=false)
    @Length(max = 64)
    @NotBlank(message = "属性字段不能为空",groups = {AddGroup.class})
    private String property;

    @ApiModelProperty(value="属性值",required=false)
    @Length(max = 1024)
    @NotBlank(message = "属性值不能为空",groups = {AddGroup.class})
    private String value;

    @ApiModelProperty(value="对应业务id",required=false)
    @Length(max = 64)
    private String leftObject;

    @ApiModelProperty(value="对应业务类型",required=false)
    @Length(max = 64)
    private String leftObjectType;

    @Override
    public String getObjectType(){
        return "ExtraPropertyData";
    }

}