package com.nancal.api.model;

import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "Gte4MfgProcess 工艺 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Gte4MfgProcessReq extends MfgProcessReq  implements Serializable{
//    public String getObjectType(){
//        return "Gte4MfgProcess";
//    }

    @NotBlank(message = "id不能为空",groups = {UpdateGroup.class})
    @Length(max = 128, message = "id超过了最大长度限制",groups = {UpdateGroup.class})
    @ApiModelProperty(value = "主键id", required = true)
    private String uid;

    @NotBlank(message = "名称",groups = {UpdateGroup.class})
    @ApiModelProperty(value="名称",required=false)
    @Length(max = 128,message = "名称超过了最大长度限制",groups = {UpdateGroup.class})
    private String objectName;

    @NotBlank(message = "密级",groups = {UpdateGroup.class})
    @ApiModelProperty(value="密级",required=false)
    @Length(max = 128,message = "密级超过了最大长度限制",groups = {UpdateGroup.class})
    private String secretLevel;

    @NotBlank(message = "保密期限",groups = {UpdateGroup.class})
    @ApiModelProperty(value="保密期限",required=false)
    @Length(max = 128,message = "保密期限超过了最大长度限制",groups = {UpdateGroup.class})
    private String secretTerm;

    @NotBlank(message = "编号",groups = {UpdateGroup.class})
    @ApiModelProperty(value="编号",required=false)
    @Length(max = 64,message = "编号超过了最大长度限制",groups = {UpdateGroup.class})
    private String partNo;
//    public String getObjectType(){
//        return "Item";
//    }

}