package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4PartChange 设计更改单 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4PartChangeReq extends Gte4ChangeReq  implements Serializable{

    @ApiModelProperty(value="更改单号",required=true)
    @QueryField(Ops.LIKE)
    @NotBlank(message = "更改单号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 64,message = "更改单号长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String changeId;

    @ApiModelProperty(value = "更改单名称",required=true)
    @QueryField(Ops.LIKE)
    @NotBlank(message = "更改单名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 128,message = "更改单名称长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String objectName;

    @ApiModelProperty(value="更改类型",required=false)
    @QueryField(Ops.EQ)
    @Length(max = 128,message = "更改类型长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ChangeType;

    @ApiModelProperty(value="生命周期状态",required=false)
    @QueryField(Ops.EQ)
    @Length(max = 64,message = "生命周期状态长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String lifeCycleState;

}