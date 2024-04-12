package com.nancal.api.model;


import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@ApiModel(value = "Gte4MaterialRevision 物料版本 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4MaterialRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="专业",required=false)
    private String gte4Special;
    @ApiModelProperty(value="负责人",required=false)
    private String gte4Responsor;
    @ApiModelProperty(value="零组件类型",required=false)
    private String gte4ItemType;
    @ApiModelProperty(value="易必件",required=false)
    private String gte4DelicatePart;
    @ApiModelProperty(value="标准件类型",required=false)
    private String gte4StdType;
    @ApiModelProperty(value="零组件类型2",required=false)
    private String gte4ItemType2;
    @ApiModelProperty(value="生产厂家",required=false)
    private String gte4Maker;
    @ApiModelProperty(value="批次号",required=false)
    private String gte4Bn;
    @ApiModelProperty(value="批次顺序号",required=false)
    private String gte4BnSn;
    @ApiModelProperty(value="位置号",required=false)
    private String gte4PosNo;
    @ApiModelProperty(value="现有材料",required=false)
    private String gte4AvailMaterial;
    @ApiModelProperty(value="额定寿命时间（成附件）",required=false)
    private String gte4RatedLifeTime;
    @ApiModelProperty(value="当次需运转时间（成附件）",required=false)
    private String gte4CurrentOperationTime;
    @ApiModelProperty(value="型号",required=false)
    private String gte4ModelNo;

    @Override
    public String getObjectType(){
       return "Gte4MaterialRevision";
    }

}