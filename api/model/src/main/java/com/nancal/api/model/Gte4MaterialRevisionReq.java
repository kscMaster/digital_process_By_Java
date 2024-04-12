package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;


@ApiModel(value = "Gte4MaterialRevision 物料版本 的请求")
@Data

@NoArgsConstructor
@AllArgsConstructor
public class Gte4MaterialRevisionReq extends ItemRevisionReq  implements Serializable{

    @Length(max = 128, message = "id超过了最大长度限制",groups = {LikeGroup.class})
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @QueryField(Ops.LIKE)
    @Length(max = 64,message = "代号长度超过了最大限制",groups = {UpdateGroup.class, LikeGroup.class})
    @ApiModelProperty(value = "代号", required = true)
    private String itemId;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "名称不能为空",groups = {UpdateGroup.class})
    @ApiModelProperty(value="名称",required=true)
    @Length(max = 128,message = "名称超过最大长度限制",groups = {LikeGroup.class})
    private String objectName;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="专业",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4Special;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="负责人",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4Responsor;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="零组件类型",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4ItemType;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="易必件",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4DelicatePart;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="标准件类型",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4StdType;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="零组件类型2",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4ItemType2;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="生产厂家",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4Maker;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="批次号",required=false)
    @Length(max = 64,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4Bn;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="批次顺序号",required=false)
    @Length(max = 64,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4BnSn;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="位置号",required=false)
    @Length(max = 64,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4PosNo;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="现有材料",required=false)
    @Length(max = 128,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4AvailMaterial;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="额定寿命时间（成附件）",required=false)
    @Length(max = 64,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4RatedLifeTime;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value="当次需运转时间（成附件）",required=false)
    @Length(max = 64,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4CurrentOperationTime;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="型号",required=false)
    @Length(max = 64,message = "超过最大长度限制",groups = {LikeGroup.class})
    private String gte4ModelNo;

}