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
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@ApiModel(value = "Gte4StationRevision 工位的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4StationRevisionReq extends MsgbImportReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="序号",required=true)
    @Length(max = 128,message = "序号长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4orderNo;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="对象名称",required=true)
    @Length(max = 128,message = "对象名称长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="对象类型",required=true)
    @Length(max = 64,message = "对象类型长度超过限制",groups = {UpdateGroup.class})
    private String objectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="备注",required=true)
    @Length(max = 512,message = "备注长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String remark;

    @ApiModelProperty(value="左对象uid",required=true)
    @Length(max = 64,groups = {UpdateGroup.class})
    @NotEmpty(message = "左对象uid不能为空")
    private String leftObject;

    @ApiModelProperty(value="左对象类型",required=true)
    @Length(max = 128,groups = {UpdateGroup.class})
    @NotEmpty(message = "左对象类型不能为空")
    private String leftObjectType;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件号",required=true)
    @Length(max = 64,groups = {LikeGroup.class})
    private String itemId;

    @QueryField(Ops.EQ)
    @NotBlank(message = "工位类型不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="工位类型",required=true)
    @Length(max = 128,message = "工位类型长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4StationType;

    @NotEmpty(message = "工位长度不能为空")
    @ApiModelProperty(value="工位长度(m)",required=true)
    private Double gte4LengthOfStation;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "ESD要求不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="ESD要求",required=true)
    @Length(max = 128,message = "ESD要求长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4ESDReq;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "洁净度要求不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="洁净度要求",required=true)
    @Length(max = 128,message = "洁净度要求长度超过限制",groups = {AddGroup.class, UpdateGroup.class})
    private String gte4CleanReq;


}