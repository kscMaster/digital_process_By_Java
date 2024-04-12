package com.nancal.api.model;

import com.nancal.common.base.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "ChangeEffectRL 受影响的 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ChangeEffectRLReq extends RelationReq  implements Serializable{

    @ApiModelProperty(value="关系类型",required=true)
    @NotBlank(message = "关系类型不能为空",groups = {AddGroup.class})
    @Length(max = 64,message = "关系类型长度超过最大限制",groups = {AddGroup.class})
    private String relationType;

    @ApiModelProperty(value = "左对象ID", required = true)
    @NotBlank(message = "左对象ID不能为空", groups = {AddGroup.class})
    @Length(max = 64,message = "左对象ID长度超过最大限制",groups = {AddGroup.class})
    private String leftObject;

    @ApiModelProperty(value="左对象类型",required=true)
    @NotBlank(message = "左对象类型不能为空", groups = {AddGroup.class})
    @Length(max = 64,message = "左对象类型长度超过最大限制",groups = {AddGroup.class})
    private String leftObjectType;

    @ApiModelProperty(value="右对象ID",required=true)
    @NotBlank(message = "右对象ID不能为空", groups = {AddGroup.class})
    @Length(max = 64,message = "右对象ID长度超过最大限制",groups = {AddGroup.class})
    private String rightObject;

    @ApiModelProperty(value="右对象类型",required=true)
    @NotBlank(message = "右对象类型不能为空", groups = {AddGroup.class})
    @Length(max = 64,message = "右对象类型长度超过最大限制",groups = {AddGroup.class})
    private String rightObjectType;

    @ApiModelProperty(value="版本号")
    @Length(max = 64)
    private String itemRevId;

    @ApiModelProperty(value="零组件id")
    @Length(max = 64)
    private String itemUid;

}