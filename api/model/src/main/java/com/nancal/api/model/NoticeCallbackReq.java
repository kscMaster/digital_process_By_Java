package com.nancal.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class NoticeCallbackReq implements Serializable {

//    @NotBlank(message = "设计更改单id不能为空或存在非法脚本信息")
    @ApiModelProperty(value = "更改单id")
    private String uid;

//    @NotBlank(message = "通知单位id不能为空或存在非法脚本信息")
    @ApiModelProperty(value = "通知单位id")
    private String noticeUid;

    @NotEmpty(message = "流程实例id不能为空")
    @ApiModelProperty(value = "流程实例ID")
    private String processInstanceId;

}
