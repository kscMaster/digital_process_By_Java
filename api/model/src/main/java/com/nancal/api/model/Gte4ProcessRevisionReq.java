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


@ApiModel(value = "Gte4ProcessRevision 工艺版本 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4ProcessRevisionReq extends MsgbImportReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "代号长度超过了最大限制",groups = {LikeGroup.class})
    @ApiModelProperty(value = "代号", required = true)
    private String itemId;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "名称不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="名称",required=true)
    @Length(max = 128,message = "名称超过最大长度限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件图代号",required=false)
    @NotBlank(message = "零组件图代号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "零组件图代号长度超过了最大限制",groups = {LikeGroup.class})
    private String gte4PartNo;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="特性分类",required=false)
//    @NotBlank(message = "特性分类不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "特性分类长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4Cat;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="材料牌号",required=false)
//    @NotBlank(message = "材料牌号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "材料牌号长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4MaterialTradeMark;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="材料技术标准",required=false)
//    @NotBlank(message = "材料技术标准不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "材料技术标准长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4MatTechStd;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="材料规格",required=false)
//    @NotBlank(message = "材料规格不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "材料规格长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4MatSpec;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="工艺批次",required=false)
//    @NotBlank(message = "设计批次不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "工艺批次长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4DznBn;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="密级",required=false)
//    @NotBlank(message = "密级不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "密级超过了最大长度限制",groups = {UpdateGroup.class,AddGroup.class,UpdateGroup.class})
    private String secretLevel;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="保密期限",required=false)
//    @NotBlank(message = "保密期限不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "保密期限超过了最大长度限制",groups = {UpdateGroup.class,AddGroup.class,UpdateGroup.class})
    private String secretTerm;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="版本所有权用户(PDM)",required=false)
//    @NotBlank(message = "版本所有权用户不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "版本所有权用户长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4PDMOwnner;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="版本所有权组(PDM)",required=false)
//    @NotBlank(message = "版本所有权组不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 128,message = "版本所有权组长度超过了最大限制",groups = {LikeGroup.class,AddGroup.class,UpdateGroup.class})
    private String gte4PDMOwningGroup;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="初始型号",required=true)
//    @NotBlank(message = "初始型号不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "初始型号长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    private String gte4InitModel;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="阶段标识",required=true)
//    @NotBlank(message = "阶段标识不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @Length(max = 64,message = "阶段标识长度超过了最大限制",groups = {AddGroup.class,UpdateGroup.class, LikeGroup.class})
    private String gte4Phase;

//    @Override
//    public String getObjectType(){
//        return "Gte4ProcessRevision";
//    }

}