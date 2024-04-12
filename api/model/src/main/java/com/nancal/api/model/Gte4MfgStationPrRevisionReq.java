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


@ApiModel(value = "Gte4MfgStationPrRevision 工位工艺版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgStationPrRevisionReq extends MsgbImportReq  implements Serializable{

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

    @QueryField(Ops.LIKE)
    @NotBlank(message = "变更记录不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="变更记录",required=true)
    @Length(max = 1024,message = "变更记录长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "变更记录",required = true,max = 1024)
    private String gte4ChangeRecord;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "特殊特性不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="特殊特性",required=true)
    @Length(max = 128,message = "特殊特性长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "特殊特性",required = true,max = 128)
    private String gte4SpecialCharacteristics;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "异常处理、注意事项、其他不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="异常处理、注意事项、其他",required=true)
    @Length(max = 1024,message = "异常处理、注意事项、其他长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "异常处理、注意事项、其他",required = true,max = 1024)
    private String gte4ExceptionCautions;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "关键信息不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="关键信息",required=true)
    @Length(max = 1024,message = "关键信息长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "关键信息",required = true,max = 1024)
    private String gte4KeyInfoProgram;

    @QueryField(Ops.EQ)
    @NotNull(message = "关键工序不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="关键工序",required=true)
    @FieldAlias(name = "关键工序&",required = true)
    private Boolean gte4KeyProcess;



}