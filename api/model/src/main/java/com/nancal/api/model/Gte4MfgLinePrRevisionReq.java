package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgLinePrRevision 线体工艺版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MfgLinePrRevisionReq extends MsgbImportReq  implements Serializable{

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
    @NotBlank(message = "过板方向不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="过板方向",required=true)
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "过板方向",required = true,max = 128)
    private String gte4TheDirection;

    @QueryField(Ops.EQ)
    @NotBlank(message = "工序分类不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="工序分类",required=true)
    @Length(max = 128,message = "工序分类长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工序分类*",required = true,max = 128)
    @JsonDict(DictConstant.GTE4_PROCESS_CLASSIFICATION)
    private String gte4ProcessClassification;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "工艺拼板模式不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="工艺拼板模式",required=true)
    @Length(max = 128,message = "工艺拼板模式长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工艺拼装模式",required = true,max = 128)
    private String gte4PanelModel;


}