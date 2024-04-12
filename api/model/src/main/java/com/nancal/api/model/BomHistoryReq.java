package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.nancal.common.base.LikeGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;


@ApiModel(value = "BomHistory bom查询历史 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BomHistoryReq extends ItemRevisionReq  implements Serializable{

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "对象名称")
    @NotBlank(message = "名称不能为空",groups = {AddGroup.class})
    @Length(max = 128,message = "对象名称长度超过了最大限制",groups = {AddGroup.class, LikeGroup.class})
    private String objectName;

    @QueryField(Ops.LIKE)
    @ApiModelProperty(value = "代号")
    @NotBlank(message = "代号不能为空",groups = {AddGroup.class})
    @Length(max = 128,message = "代（图）号的长度不能大于128",groups = {AddGroup.class, LikeGroup.class})
    private String itemId;

    @QueryField(Ops.EQ)
    @ApiModelProperty(value="BOM视图",required=true)
    @NotBlank(message = "BOM视图不能为空",groups = {AddGroup.class,FidHistoryGroup.class,LikeGroup.class})
    @Length(max = 128,message = "BOM视图长度超过了最大限制",groups = {AddGroup.class, FidHistoryGroup.class,LikeGroup.class})
    private String bomView;

    @ApiModelProperty(value="查询的版本uid",required=true)
    @NotBlank(message = "查询的版本uid不能为空",groups = {AddGroup.class})
    @Length(max = 64,message = "查询的版本uid长度超过了最大限制",groups = {AddGroup.class, LikeGroup.class})
    private String revUid;

    @ApiModelProperty(value="查询的对象类型",required=true)
    @NotBlank(message = "查询的对象类型不能为空",groups = {AddGroup.class})
    @Length(max = 64,message = "查询的对象类型长度超过了最大限制",groups = {AddGroup.class, LikeGroup.class})
    private String revObjectType;

    @ApiModelProperty(value="版本号",required=true)
    @NotBlank(message = "版本号不能为空",groups = {AddGroup.class})
    @Length(max = 64, message = "版本号长度不能大于128",groups = {AddGroup.class, LikeGroup.class})
    private String revisionId;

    public String getObjectType(){
        return "BomHistory";
    }

}