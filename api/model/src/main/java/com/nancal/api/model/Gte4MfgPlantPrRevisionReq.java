package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgPlantPrRevision 工厂工艺版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgPlantPrRevisionReq extends MsgbImportReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="代号ID",required=true)
    @Length(max = 64,message = "代号ID超过最大长度限制",groups = {LikeGroup.class})
    private String itemId;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "对象名称")
    @NotBlank(message = "名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "名称",required = true,max = 128)
    private String objectName;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="是否无铅加工",required=true)
    @NotNull(message = "是否无铅加工不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @FieldAlias(name = "是否无铅加工&",required = true)
    private Boolean gte4LeadFree;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "静电防护等级不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="静电防护等级",required=true)
    @Length(max = 64,message = "静电防护等级长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "静电防护等级",required = true,max = 64)
    private String gte4ELectrostaticProtection;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "防尘等级不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="防尘等级",required=true)
    @Length(max = 64,message = "防尘等级长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "防尘等级",required = true,max = 64)
    private String gte4DustRating;

    @QueryField(Ops.EQ)
    @NotNull(message = "PCB尺寸-长不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="PCB尺寸-长",required=true)
    @FieldAlias(name = "PCB尺寸-长#",required = true)
    private Double gte4PCBSizeLength;

    @QueryField(Ops.EQ)
    @NotNull(message = "PCB尺寸-宽不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="PCB尺寸-宽#",required=true)
    @FieldAlias(name = "PCB尺寸-宽#",required = true)
    private Double gte4PCBSizeWidth;

    @QueryField(Ops.EQ)
    @NotNull(message = "PCB尺寸-高不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="PCB尺寸-高",required=true)
    @FieldAlias(name = "PCB尺寸-高#",required = true)
    private Double gte4PCBSizeHeigth;


}