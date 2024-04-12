package com.nancal.api.model;

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

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4CnstrProcessRevision 施工过程卡版本 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4CnstrProcessRevisionReq extends Gte4MfgProcessRevisionReq  implements Serializable{

    @QueryField(Ops.EQ)
//    @NotBlank(message = "第几次装配/分解不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="第几次装配/分解",required=true)
    @Max(Integer.MAX_VALUE)
    private Integer gte4Time;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "生产订单号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="生产订单号",required=true)
    @Length(max = 64,message = "生产订单号超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4Order;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "生产工单号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="生产工单号",required=true)
    @Length(max = 64,message = "生产工单号超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4WorkOrder;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "台份号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="台份号",required=true)
    @Length(max = 64,message = "台份号超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4UnitNo;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "批次号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="批次号",required=true)
    @Length(max = 64,message = "批次号超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4Bn;

    @QueryField(Ops.LIKE)
//    @NotBlank(message = "正式工艺规程编号不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="正式工艺规程编号",required=true)
    @Length(max = 64,message = "正式工艺规程编号超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ProcessCode;

//    @NotBlank(message = "正式工艺规程ID不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value="正式工艺规程ID",required=true)
    @Length(max = 64,message = "正式工艺规程ID超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ProcessId;

//    @NotBlank(message = "正式工艺规程版本不能为空",groups = {AddGroup.class})
    @ApiModelProperty(value="正式工艺规程版本",required=true)
    @Length(max = 64,message = "正式工艺规程版本超过了最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String gte4ProcessRev;

    @QueryField(Ops.LIKE)
    @NotBlank(message = "编制单位不能为空",groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value="编制单位",required=true)
    @Length(max = 128,message = "编制单位超过最大长度限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    private String ownerGroupName;

//    public String getObjectType(){
//        return "Gte4CnstrProcess";
//    }

}